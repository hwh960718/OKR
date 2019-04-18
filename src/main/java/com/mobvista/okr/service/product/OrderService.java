package com.mobvista.okr.service.product;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.config.ApplicationProperties;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.OrderInfo;
import com.mobvista.okr.domain.ProductShelf;
import com.mobvista.okr.domain.ScoreUserDetail;
import com.mobvista.okr.dto.product.OrderEmailInfoDTO;
import com.mobvista.okr.dto.product.OrderInfoDTO;
import com.mobvista.okr.dto.score.ScoreUserDTO;
import com.mobvista.okr.enums.product.OrderStatusEnum;
import com.mobvista.okr.enums.product.ProductShelfStatusEnum;
import com.mobvista.okr.enums.product.ProductShelfTypeEnum;
import com.mobvista.okr.enums.score.ScoreRelatedFlagEnum;
import com.mobvista.okr.enums.score.ScoreRuleTypeEnum;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.DateUtil;
import com.mobvista.okr.util.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：订单服务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/14 18:09
 */
@Service
public class OrderService {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private ApplicationProperties applicationProperties;

    @Resource
    private OrderInfoRepository orderInfoRepository;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private ProductShelfRepository productShelfRepository;
    @Resource
    private ScoreUserRepository scoreUserRepository;
    @Resource
    private ProductRedisService orderRedisService;
    @Resource
    private ScoreUserDetailRepository scoreUserDetailRepository;
    @Resource
    private ProductRedisService productRedisService;
    @Resource
    private ProductEmailService productEmailService;
    @Resource
    private OrderEmailService orderEmailService;

    /**
     * 分页查询
     *
     * @param dto
     * @param pageable
     * @return
     */
    public CommonResult list(OrderInfoDTO dto, Pageable pageable) {
        int count = orderInfoRepository.countListByDto(dto);
        List<OrderInfoDTO> list = Lists.newArrayList();
        if (count > 0) {
            list = orderInfoRepository.queryListByDto(dto, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            list.forEach(order -> {
                order.setShelfTypeText(ProductShelfTypeEnum.getTextByCode(order.getShelfType()));
                order.setStatusText(OrderStatusEnum.getValueByCode(order.getStatus()));
                order.setAdminRole(dto.isAdminRole());
            });
        }
        return CommonResult.success(new PageImpl<>(list, pageable, count));
    }


    /**
     * 创建订单
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult saveOrderInfo(OrderInfoDTO dto) {
        try {
            log.info("创建订单 ---- 开始");
            if (null == dto) {
                log.info("创建订单 ---- 入参为空");
                return CommonResult.error("入参为空");
            }
            //校验请求是否是合法请求
            if (applicationProperties.isCheckOrderPassword() && !checkInputPassword(dto)) {
                return CommonResult.error("非法请求");
            }
            Long shelfId = dto.getShelfId();
            ProductShelf productShelf = productShelfRepository.selectByPrimaryKey(shelfId);
            //校验上架信息
            if (null == productShelf) {
                log.info("创建订单 ---- 上架资产不存在，上架id={}", shelfId);
                return CommonResult.error("上架资产不存在");
            }
            if (ProductShelfStatusEnum.DOWN.getCode() == productShelf.getStatus()) {
                log.info("创建订单 ---- 项目已下架，上架id={}", shelfId);
                return CommonResult.error("项目已下架");
            }
            Date now = new Date();
            if (!now.after(productShelf.getValidDateStart())) {
                log.info("创建订单 ---- 项目未开始，上架id={}，当前时间：{}，上架开始时间：{}", shelfId, DateUtil.toString(now), DateUtil.toString(productShelf.getValidDateStart()));
                return CommonResult.error("项目未开始");
            }
            if (!now.before(productShelf.getValidDateEnd())) {
                log.info("创建订单 ---- 项目已结束，上架id={}，当前时间：{}，上架结束时间：{}", shelfId, DateUtil.toString(now), DateUtil.toString(productShelf.getValidDateEnd()));
                return CommonResult.error("项目已结束");
            }
            //判断上架类型
            if (ProductShelfTypeEnum.EXCHANGE.getCode() == productShelf.getType()) {
                return exchangeOrder(dto, productShelf);
            } else if (ProductShelfTypeEnum.AUCTION.getCode() == productShelf.getType()) {
                return auctionOrder(dto, productShelf);
            }
            return CommonResult.error("未知下单类型");
        } catch (Exception e) {
            log.error("创建订单 ---- 异常", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            log.info("创建订单 ---- 结束");
        }
    }

    /**
     * 竞拍订单处理
     *
     * @param dto
     * @param productShelf
     * @return
     */
    private synchronized CommonResult auctionOrder(OrderInfoDTO dto, ProductShelf productShelf) {
        if (BigDecimal.ZERO.compareTo(dto.getPrice()) >= 0) {
            return CommonResult.error("竞拍出价不能为负数");
        }
        //需要锁定的积分
        int lockScore = dto.getPrice().intValue();
        Long productShelfId = productShelf.getId();
        BigDecimal divide = BigDecimal.valueOf(lockScore).subtract(productShelf.getPrice());
        //判断是否是倍数竞拍
        if (divide.intValue() % productShelf.getScoreIncrement().intValue() > 0) {
            return CommonResult.error("请按照增加幅度倍数竞拍");
        }
        BigDecimal currentPrice = productRedisService.getCurrentProductShelfOrderPrice(productShelfId);
        if (currentPrice.compareTo(dto.getPrice()) >= 0) {
            return CommonResult.error("出价必须大于历史出价");
        }
        try {
            //设置当前商品竞价
            productRedisService.setCurrentProductShelfOrderPrice(productShelfId, dto.getPrice());
            //查询当前有效订单
            List<OrderEmailInfoDTO> orderEmailInfoDTOS = orderInfoRepository.queryOrderInfoByShelfIdAndStatus(productShelfId, OrderStatusEnum.Locked.getCode());
            Long currentUserId = SecurityUtils.getCurrentUserId();
            //需要解锁的用户
            List<ScoreUserDTO> unLockScoreUserList = Lists.newArrayList();
            List<Long> disableOrderIdList = Lists.newArrayList();
            if (null != orderEmailInfoDTOS && orderEmailInfoDTOS.size() > 0) {
                for (OrderEmailInfoDTO o : orderEmailInfoDTOS) {
                    if (o.getPrice().compareTo(dto.getPrice()) >= 0) {
                        throw new RuntimeException("出价必须大于历史出价");
                    }
                    disableOrderIdList.add(o.getOrderId());
                    //更加相同用户获取实际其锁定积分
                    if (o.getCreateUserId().equals(currentUserId)) {
                        lockScore -= o.getPrice().intValue();
                    } else {
                        //需要解锁的用户积分
                        assembleScoreUserList(unLockScoreUserList, o.getCreateUserId(), -o.getPrice().intValue());
                    }
                }
            } //需要锁定的用户积分
            List<ScoreUserDTO> lockScoreUserList = Lists.newArrayList();
            //需要锁定的用户
            assembleScoreUserList(lockScoreUserList, currentUserId, lockScore);
            //锁定用户积分
            int i = scoreUserRepository.updateUserLockScoreList(lockScoreUserList);
            if (i == 0) {
                throw new RuntimeException("用户积分不足");
            }
            //解锁其他用户积分
            if (unLockScoreUserList.size() > 0) {
                scoreUserRepository.updateUserLockScoreList(unLockScoreUserList);
            }
            //新增订单
            insertOrderInfo(productShelf, CommonConstants.NUM_1, dto.getPrice(), currentUserId, OrderStatusEnum.Locked.getCode());
            //批量修改订单状态为无效订单
            if (disableOrderIdList.size() > 0) {
                orderInfoRepository.updateOrderStatusByIdList(disableOrderIdList, OrderStatusEnum.UnLocked.getCode());
            }
            //邮件提醒
            //竞拍成功邮件提醒
            orderEmailService.orderAuctionLockRemind(productShelfId, dto.getPrice());
            //竞拍释放邮件提醒
            orderEmailService.orderAuctionUnLockRemind(orderEmailInfoDTOS, dto.getPrice());
        } catch (RuntimeException e) {
            //还原当前竞价
            productRedisService.setCurrentProductShelfOrderPrice(productShelfId, currentPrice);
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("竞拍订单处理----异常", e);
            throw new RuntimeException("竞拍订单处理异常");
        }
        return CommonResult.success();
    }


    /**
     * 处理竞拍项目订单
     *
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult processAuctionProductShelfOrder() {
        //查询所有的锁定订单对应的上架项目
        OrderInfo queryOrderInfo = new OrderInfo();
        queryOrderInfo.setStatus(OrderStatusEnum.Locked.getCode());
        List<OrderInfo> lockedOrderList = orderInfoRepository.queryOrderInfo(queryOrderInfo);
        if (null == lockedOrderList || lockedOrderList.size() == 0) {
            log.info("处理竞拍项目订单----无竞拍中的订单");
            return CommonResult.success();
        }
        List<Long> shelfIdList = Lists.newArrayList();
        Map<Long, OrderInfo> shelfOrderMap = Maps.newHashMap();
        lockedOrderList.forEach(orderInfo -> {
            shelfIdList.add(orderInfo.getShelfId());
            shelfOrderMap.put(orderInfo.getShelfId(), orderInfo);
        });
        List<ProductShelf> productShelfList = productShelfRepository.queryListByProductIdAndStatus(null, shelfIdList, null);
        if (null == productShelfList || productShelfList.size() == 0) {
            log.info("处理竞拍项目订单----无对应上架商品");
            return CommonResult.success();
        }
        //需要修改状态的订单id
        List<Long> modifyOrderIdList = Lists.newArrayList();
        //需要下架的 上架商品id
        List<Long> downProductShelfIdList = Lists.newArrayList();
        List<ProductShelf> downProductShelf = Lists.newArrayList();
        //需要解锁的用户
        List<ScoreUserDTO> unLockScoreUserList = Lists.newArrayList();
        //用户积分明细
        List<ScoreUserDetail> scoreUserDetailList = Lists.newArrayList();
        for (ProductShelf productShelf : productShelfList) {
            //当前上架项目状态为下架 或者
            if (ProductShelfStatusEnum.isDown(productShelf.getStatus()) || !productShelf.getValidDateEnd().after(new Date())) {
                //获取当前上架项目 对应的锁定订单
                Long productShelfId = productShelf.getId();
                OrderInfo orderInfo = shelfOrderMap.get(productShelfId);
                downProductShelfIdList.add(productShelfId);
                downProductShelf.add(productShelf);
                modifyOrderIdList.add(orderInfo.getId());
                //需要解锁的用户积分
                assembleScoreUserList(unLockScoreUserList, orderInfo.getCreateUserId(), orderInfo.getPrice().intValue());
                assembleScoreUserDetailList(scoreUserDetailList, productShelf, orderInfo);
            }
        }
        //修改订单状态
        if (modifyOrderIdList.size() > 0) {
            orderInfoRepository.updateOrderStatusByIdList(modifyOrderIdList, OrderStatusEnum.Enable.getCode());
        }
        //解锁用户积分并修改可用积分和消减积分数
        if (unLockScoreUserList.size() > 0) {
            scoreUserRepository.updateUserValidAbatementUnLockScoreList(unLockScoreUserList);
        }
        //插入积分明细
        if (scoreUserDetailList.size() > 0) {
            scoreUserDetailRepository.insertList(scoreUserDetailList);
        }
        //下架 上架商品
        if (downProductShelfIdList.size() > 0) {
            productShelfRepository.updateAuctionProductShelfStatusEndByIdList(downProductShelfIdList, ProductShelfStatusEnum.END.getCode());
        }
        //接收库存为已使用
        if (downProductShelf.size() > 0) {
            for (ProductShelf productShelf : downProductShelf) {
                productRepository.updateProductUnLock2UsedInventory(productShelf.getProductId(), productShelf.getShelfCount());
            }
        }
        // 触发邮件提醒
        productEmailService.productShelfStatusEndRemind(downProductShelfIdList, true);
        //触发订单成功提醒
        orderEmailService.orderAuctionSuccessRemind(modifyOrderIdList);
        return CommonResult.success();
    }


    /**
     * 组装需要锁定的用户积分
     *
     * @param scoreUserList
     * @param id
     * @param lockScore
     */
    private void assembleScoreUserList(List<ScoreUserDTO> scoreUserList, Long id, long lockScore) {
        ScoreUserDTO scoreUserDto = new ScoreUserDTO();
        scoreUserDto.setId(id);
        scoreUserDto.setLockedScoreTotal(lockScore);
        scoreUserDto.setValidScoreTotal(lockScore);
        scoreUserList.add(scoreUserDto);
    }


    /**
     * 处理订单失败 将状态修改为已处理
     *
     * @param orderId
     * @return
     */
    public CommonResult processedOrderStatus(Long orderId) {
        if (null == orderId || orderId <= 0) {
            return CommonResult.error("订单id为空");
        }
        OrderInfo orderInfo = orderInfoRepository.selectByPrimaryKey(orderId);
        if (null == orderInfo) {
            return CommonResult.error("订单不存在");
        }
        //判断订单是否为已完成订单，否则不可修改状态
        if (!orderInfo.getStatus().equals(OrderStatusEnum.Enable.getCode())) {
            return CommonResult.error("状态不可修改");
        }
        OrderInfo update = new OrderInfo();
        update.setId(orderId);
        update.setStatus(OrderStatusEnum.Processed.getCode());
        update.setModifyUserId(SecurityUtils.getCurrentUserId());
        int i = orderInfoRepository.updateOrderStatusById(update);
        if (i > 0) {
            return CommonResult.success();
        }
        return CommonResult.error("处理订单失败");
    }

    /**
     * 兑换订单处理
     *
     * @param dto
     * @param productShelf
     * @return
     */
    private CommonResult exchangeOrder(OrderInfoDTO dto, ProductShelf productShelf) {
        Long currentUserId = SecurityUtils.getCurrentUserId();

        try {
            if (!productRedisService.orderLock(productShelf.getId(), currentUserId, RedisConstants.REDIS_LOCK_EXPIRED_TIME_5S)) {
                return CommonResult.error("重复下单");
            }
            log.info("兑换订单处理----订单信息：{}", JSON.toJSONString(dto));
            //判断用户可用积分是否充足
            Integer num = dto.getOrderNum();
            if (num <= 0) {
                return CommonResult.error("请选择有效的兑换数量");
            }
            //判断上架库存是否充足，并扣除库存
            int updateProductUseCount = productShelfRepository.updateProductUseCount(productShelf.getId(), num);
            if (updateProductUseCount == 0) {
                throw new RuntimeException("可兑换库存不足");
            }
            //判断库存是否充足，并扣除库存
            Long productId = productShelf.getProductId();
            int updateNumProductInventory = productRepository.updateProductUnLock2UsedInventory(productId, num);
            if (updateNumProductInventory == 0) {
                throw new RuntimeException("商品可用数量不足");
            }
            //兑换所耗积分数
            BigDecimal price = productShelf.getPrice().multiply(BigDecimal.valueOf(num));
            int score = price.intValue();
            //判断用户积分数是否充足
            int updateNumScore = scoreUserRepository.updateUserValidScore2AbatementScore(currentUserId, score);
            if (updateNumScore == 0) {
                throw new RuntimeException("可用积分数量不足");
            }
            //生成订单记录
            OrderInfo orderInfo = insertOrderInfo(productShelf, num, price, currentUserId, OrderStatusEnum.Enable.getCode());
            //生成积分消减明细
            insertScoreUserDetail(productShelf, orderInfo);
            OrderEmailInfoDTO orderEmailInfoDTO = orderInfoRepository.queryOrderInfoByOrderId(orderInfo.getId());
            orderEmailService.orderExchangeSuccessRemind(orderEmailInfoDTO);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            productRedisService.orderUnLock(productShelf.getId(), currentUserId);
        }
        return CommonResult.success("兑换成功");
    }

    /**
     * 保存用户积分明细记录
     *
     * @param productShelf
     * @param orderInfo
     */
    private void insertScoreUserDetail(ProductShelf productShelf, OrderInfo orderInfo) {
        scoreUserDetailRepository.insert(assembleScoreUserDetail(productShelf, orderInfo));
    }


    /**
     * 组装用户积分明细
     *
     * @param productShelf
     * @param orderInfo
     * @return
     */
    private ScoreUserDetail assembleScoreUserDetail(ProductShelf productShelf, OrderInfo orderInfo) {
        ScoreUserDetail scoreUserDetail = new ScoreUserDetail();
        scoreUserDetail.setType(ScoreRuleTypeEnum.Abatement.getCode());
        scoreUserDetail.setScore(orderInfo.getPrice().longValue());
        scoreUserDetail.setRelatedId(orderInfo.getId());
        scoreUserDetail.setRelatedFlag(ScoreRelatedFlagEnum.ProductOrder.getCode());
        scoreUserDetail.setRelatedDesc(JSON.toJSONString(productShelf) + JSON.toJSONString(orderInfo));
        scoreUserDetail.setUserId(orderInfo.getCreateUserId());
        scoreUserDetail.setCreateDate(new Date());
        return scoreUserDetail;
    }


    /**
     * 组装用户积分明细集合
     *
     * @param scoreUserDetailList
     * @param productShelf
     * @param orderInfo
     */
    private void assembleScoreUserDetailList(List<ScoreUserDetail> scoreUserDetailList, ProductShelf productShelf, OrderInfo orderInfo) {
        scoreUserDetailList.add(assembleScoreUserDetail(productShelf, orderInfo));
    }

    /**
     * 保存用户订单明细记录
     *
     * @param productShelf
     * @param num
     * @param price
     * @param currentUserId
     * @return
     */
    private OrderInfo insertOrderInfo(ProductShelf productShelf, Integer num, BigDecimal price, Long currentUserId, Byte status) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderRedisService.getOrderNo());
        orderInfo.setProductId(productShelf.getProductId());
        orderInfo.setShelfId(productShelf.getId());
        orderInfo.setShelfType(productShelf.getType());
        orderInfo.setPrice(price);
        orderInfo.setOrderNum(num);
        orderInfo.setStatus(status);
        orderInfo.setCreateDate(new Date());
        orderInfo.setCreateUserId(currentUserId);
        orderInfoRepository.insert(orderInfo);
        return orderInfo;
    }


    private OrderInfo convertDto2Po(OrderInfoDTO dto) {
        OrderInfo po = new OrderInfo();
        BeanUtils.copyProperties(dto, po);
        return po;
    }

    /**
     * 比较入参密码校验
     *
     * @param dto
     * @return true 合法请求；false 非法请求
     */
    private boolean checkInputPassword(OrderInfoDTO dto) {
        //入参为空
        if (null == dto) {
            return false;
        }
        if (StringUtils.isBlank(dto.getPassWord())) {
            return false;
        }
        //获取需要加密的字符串
        String input = dto.getShelfId() + CommonConstants.APPEND_2_OKR + dto.getOrderNum() + CommonConstants.APPEND_2_OKR + dto.getPrice();
        String md5 = MD5Utils.makeMd5(input);
        return md5.equals(dto.getPassWord());
    }

}

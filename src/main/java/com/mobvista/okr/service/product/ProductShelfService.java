package com.mobvista.okr.service.product;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.OrderInfo;
import com.mobvista.okr.domain.ProductShelf;
import com.mobvista.okr.dto.product.ProductShelfDTO;
import com.mobvista.okr.enums.product.OrderStatusEnum;
import com.mobvista.okr.enums.product.ProductShelfStatusEnum;
import com.mobvista.okr.enums.product.ProductShelfTypeEnum;
import com.mobvista.okr.enums.product.ProductUnitsEnum;
import com.mobvista.okr.repository.OrderInfoRepository;
import com.mobvista.okr.repository.ProductRepository;
import com.mobvista.okr.repository.ProductShelfRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品上架
 *
 * @author Weier Gu (顾炜)
 * @version V3.1
 * @date 2018/9/14 11:09
 */
@Service
public class ProductShelfService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ProductShelfService.class);
    @Resource
    private ProductShelfRepository productShelfRepository;
    @Resource
    private ProductRepository productRepository;
    @Resource
    private OrderInfoRepository orderInfoRepository;
    @Resource
    private ProductEmailService productEmailService;


    /**
     * 分页查询上架信息
     *
     * @return
     */
    public CommonResult list(ProductShelfDTO dto, Pageable pageable, boolean needPicture) {
        int count = productShelfRepository.countListByDto(dto);
        List<ProductShelfDTO> list = Lists.newArrayList();
        if (count > 0) {
            list = productShelfRepository.queryListByDto(dto, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
            List<Long> shelfIdList = list.stream().map(po -> po.getId()).collect(Collectors.toList());
            Map<Long, BigDecimal> shelfOrderPriceMap = assembleShelfOrderPriceMap(shelfIdList);
            list.forEach(productShelfDTO -> {
                assembleDto(productShelfDTO, needPicture, shelfOrderPriceMap);
            });
        }
        return CommonResult.success(new PageImpl<>(list, pageable, count));
    }

    private Map<Long, BigDecimal> assembleShelfOrderPriceMap(List<Long> shelfIdList) {
        Map<Long, BigDecimal> map = Maps.newConcurrentMap();
        List<OrderInfo> list = orderInfoRepository.queryOrderInfoListByIdListAndStatusList(shelfIdList, Lists.newArrayList(OrderStatusEnum.Enable.getCode(), OrderStatusEnum.Locked.getCode()));
        for (OrderInfo orderInfo : list) {
            BigDecimal price = map.get(orderInfo.getShelfId());
            if (null == price || price.compareTo(orderInfo.getPrice()) < 0) {
                map.put(orderInfo.getShelfId(), orderInfo.getPrice());
            }
        }
        return map;
    }

    /**
     * 组装dto信息
     *
     * @param productShelfDTO
     */
    private void assembleDto(ProductShelfDTO productShelfDTO, boolean needPicture, Map<Long, BigDecimal> orderPriceMap) {
        productShelfDTO.setTypeText(ProductShelfTypeEnum.getTextByCode(productShelfDTO.getType()));
        productShelfDTO.setStatusText(ProductShelfStatusEnum.getValueByCode(productShelfDTO.getStatus()));
        productShelfDTO.setSystemTimeMillis(System.currentTimeMillis());
        if (needPicture) {
            String picturePath = productShelfDTO.getPicturePath();
            if (StringUtils.isNotBlank(picturePath)) {
                ArrayList<String> pictureList = Lists.newArrayList(picturePath.split(CommonConstants.APPEND_COMMA));
                productShelfDTO.setPicturePathList(AWS3ClientUtils.getTempLinkUrlDtoListByKeys(pictureList));
            }
        }
        if (null != orderPriceMap) {
            BigDecimal price = orderPriceMap.get(productShelfDTO.getId());
            productShelfDTO.setCurrentPrice(null == price ? productShelfDTO.getPrice() : price);
        }
    }


    /**
     * 根据上架id查询上架信息
     *
     * @param productShelfId
     * @return
     */
    public CommonResult queryProductShelfInfoById(Long productShelfId) {
        ProductShelfDTO dto = productShelfRepository.queryProductShelfInfoById(productShelfId);
        if (null == dto) {
            return CommonResult.error("上架信息不存在");
        }
        assembleDto(dto, false, null);
        return CommonResult.success(dto);
    }

    /**
     * 根据可用上架id查询上架信息
     *
     * @param productShelfId
     * @return
     */
    public CommonResult queryEnableProductShelfInfoById(Long productShelfId) {
        ProductShelfDTO dto = productShelfRepository.queryProductShelfInfoById(productShelfId);
        if (null == dto) {
            return CommonResult.error("上架信息不存在");
        }
        if (ProductShelfStatusEnum.isDown(dto.getStatus())) {
            return CommonResult.error("商品已下架");
        }
        assembleDto(dto, true, null);
        //判断当前上架项目类型
        if (ProductShelfTypeEnum.AUCTION.getCode() == dto.getType()) {
            //封装当前价格
            dto.setCurrentPrice(dto.getPrice());
            //1 查询当前有效订单
            List<OrderInfo> orderInfoList = orderInfoRepository.queryOrderInfoListByIdAndStatusList(productShelfId, Lists.newArrayList(OrderStatusEnum.Enable.getCode(), OrderStatusEnum.Locked.getCode()));
            if (null != orderInfoList && orderInfoList.size() > 0) {
                OrderInfo info = orderInfoList.get(0);
                dto.setCurrentPrice(info.getPrice());
            }
        }
        return CommonResult.success(dto);
    }


    /**
     * 保存上架信息
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult saveShelfInfo(ProductShelfDTO dto) {
        if (null == dto) {
            return CommonResult.error("入参不能为空");
        }
        ProductShelf po = convertDto2Po(dto, false);
        po.setShelfCount(dto.getShelfValidCount());
        po.setUnits(ProductUnitsEnum.Score.getCode());
        po.setShelfUseCount(CommonConstants.NUM_0);
        int i = productRepository.updateProductLockInventory(dto.getProductId(), dto.getShelfValidCount());
        if (i == 0) {
            return CommonResult.error("可用库存不足");
        }
        productShelfRepository.insert(po);
        //邮件统计上架
        productEmailService.productShelfStatusUpRemind(Lists.newArrayList(po.getId()));
        return CommonResult.success();
    }

    /**
     * 修改上架信息
     *
     * @param dto
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult updateShelfInfo(ProductShelfDTO dto) {
        if (null == dto) {
            return CommonResult.error("入参不能为空");
        }
        Long productShelfId = dto.getId();
        ProductShelf productShelf = productShelfRepository.selectByPrimaryKey(productShelfId);
        if (null == productShelf) {
            return CommonResult.error("上架信息不存在");
        }

        Byte oldStatus = productShelf.getStatus();
        if (ProductShelfStatusEnum.isEnd(oldStatus)) {
            return CommonResult.error("已结束的上架项目不能编辑");
        }
        Byte newStatus = dto.getStatus();
        Date now = new Date();
        if (ProductShelfStatusEnum.isUp(oldStatus) && ProductShelfStatusEnum.isDown(newStatus)) {
            //上架 修改为 下架
            //项目开始时间早于当前时间
            if (!productShelf.getValidDateStart().after(now) || !productShelf.getValidDateEnd().before(now)) {
                return CommonResult.error("上架销售中，不能下架");
            }

        } else if (ProductShelfStatusEnum.isDown(oldStatus) && ProductShelfStatusEnum.isUp(newStatus)) {
            // 下架 修改为 上架
            //上架结束时间早于当前时间
            if (!dto.getValidDateEnd().after(now)) {
                return CommonResult.error("销售结束时间不能早于当前时间");
            }
        }
        ProductShelf updatePo = convertDto2Po(dto, true);
        //更新锁定库存
        Integer oldCount = productShelf.getShelfCount() - productShelf.getShelfUseCount();
        Integer newCount = dto.getShelfValidCount();
        int i = productRepository.updateProductLockInventory(dto.getProductId(), newCount - oldCount);
        if (i == 0) {
            return CommonResult.error("可用库存不足");
        }
        updatePo.setShelfCount(productShelf.getShelfCount() + newCount - oldCount);
        productShelfRepository.updateByPrimaryKeySelective(updatePo);
        //新状态时上架，并且老的状态为非上架状态时，邮件提醒
        if (ProductShelfStatusEnum.isUp(newStatus) && !ProductShelfStatusEnum.isUp(oldStatus)) {
            //邮件通知
            productEmailService.productShelfStatusUpRemind(Lists.newArrayList(productShelfId));
        }
        return CommonResult.success();
    }


    /**
     * 处理兑换类商家项目 结束
     *
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void processExchangeProductShelfDown() {
        //兑换类商品 下架条件： 剩余兑换库存不足  有效结束时间小于当前时间
        ProductShelfDTO dto = new ProductShelfDTO();
        dto.setStatus(ProductShelfStatusEnum.UP.getCode());
        List<ProductShelfDTO> productShelfDTOList = productShelfRepository.queryListByDto(dto, 0, Integer.MAX_VALUE);
        if (null == productShelfDTOList || productShelfDTOList.size() == 0) {
            return;
        }

        List<Long> allShelfIdList = Lists.newArrayList();
        //可用时间过期
        List<Long> validDateEndShelfIdList = Lists.newArrayList();
        List<ProductShelfDTO> validDateEndShelfList = Lists.newArrayList();
        //库存不足
        List<Long> nullInventoryShelfIdList = Lists.newArrayList();
        //获取需要下架的 商品
        for (ProductShelfDTO productShelfDTO : productShelfDTOList) {
            if (productShelfDTO.getShelfValidCount() == 0) {
                nullInventoryShelfIdList.add(productShelfDTO.getId());
            } else if (!productShelfDTO.getValidDateEnd().after(new Date()) && productShelfDTO.getShelfValidCount() > 0) {
                validDateEndShelfIdList.add(productShelfDTO.getId());
                validDateEndShelfList.add(productShelfDTO);
            }
        }
        allShelfIdList.addAll(validDateEndShelfIdList);
        allShelfIdList.addAll(nullInventoryShelfIdList);
        //判断是否需要邮寄提醒
        if (allShelfIdList.size() == 0) {
            return;
        }
        //释放商品锁定库存
        if (validDateEndShelfList.size() > 0) {
            for (ProductShelfDTO productShelfDTO : validDateEndShelfList) {
                productRepository.updateProductLockInventory(dto.getProductId(), -productShelfDTO.getShelfValidCount());
            }
        }
        //修改状态为下架
        productShelfRepository.updateExchangeProductShelfStatusDownByIdList(allShelfIdList, ProductShelfStatusEnum.DOWN.getCode());
        // 触发邮件提醒
        productEmailService.productShelfStatusEndRemind(allShelfIdList, false);
    }

    /**
     * 自动上架商品
     *
     * @return
     */
    public void autoUpProductShelf() {
        //获取需要自动上架的商品id
        List<Long> shelfIdList = productShelfRepository.queryAutoUpProductShelfId();
        if (null == shelfIdList || shelfIdList.size() == 0) {
            return;
        }
        //获取有效时间范围内的商品
        int count = productShelfRepository.autoUpProductShelf();
        if (count == 0) {
            log.info("自动上架商品----需要自动上架的商品信息为空");
            return;
        }
        //自动上架
        log.info("自动上架商品----需要自动上架的商品的数量为：" + count);
        //自动给上架商品
        productEmailService.productShelfStatusUpRemind(shelfIdList);
    }


    private ProductShelf convertDto2Po(ProductShelfDTO dto, boolean isModify) {
        ProductShelf po = new ProductShelf();
        BeanUtils.copyProperties(dto, po);
        Date date = new Date();
        Long userId = SecurityUtils.getCurrentUserId();
        if (!isModify) {
            po.setCreateDate(date);
            po.setCreateUserId(userId);
        }
        po.setModifyDate(date);
        po.setModifyUserId(userId);
        return po;
    }
}




package com.mobvista.okr.service.product;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.constants.ProductEmailConstants;
import com.mobvista.okr.domain.OrderInfo;
import com.mobvista.okr.dto.product.ProductShelfDTO;
import com.mobvista.okr.enums.product.OrderStatusEnum;
import com.mobvista.okr.enums.product.ProductShelfStatusEnum;
import com.mobvista.okr.repository.OrderInfoRepository;
import com.mobvista.okr.repository.ProductShelfRepository;
import com.mobvista.okr.service.BaseEmailService;
import com.mobvista.okr.service.UserService;
import com.mobvista.okr.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：商品邮件服务类
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/15 11:00
 */
@Service
public class ProductEmailService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ProductEmailService.class);

    @Resource
    private BaseEmailService baseEmailService;
    @Resource
    private ProductShelfRepository productShelfRepository;
    @Resource
    private UserService userService;
    @Resource
    private OrderInfoRepository orderInfoRepository;

    /**
     * 上架商品上架提醒
     */
    @Async
    public void productShelfStatusUpRemind(List<Long> productShelfIdList) {
        if (null == productShelfIdList || productShelfIdList.size() == 0) {
            log.error("上架商品上架提醒----入参为空");
            return;
        }
        //获取上架信息
        List<ProductShelfDTO> productShelfDTOList = productShelfRepository.queryProductShelfInfoByIdList(productShelfIdList);
        if (null == productShelfDTOList || productShelfDTOList.size() == 0) {
            log.error("上架商品上架提醒----上架id({})的上架信息不存在", JSON.toJSONString(productShelfIdList));
            return;
        }

        List<ProductShelfDTO> needRemindDtoList = Lists.newArrayList();

        for (ProductShelfDTO productShelfDTO : productShelfDTOList) {
            //判断是否是上架状态
            if (ProductShelfStatusEnum.UP.getCode() == productShelfDTO.getStatus()) {
                needRemindDtoList.add(productShelfDTO);
            }
        }
        //判断是否需要发送邮件
        if (needRemindDtoList.size() == 0) {
            return;
        }
        Context context = new Context();
        context.setVariable("productShelfDTOList", needRemindDtoList);
        //所有当前用户的邮箱
        String[] userEmailArr = queryAllOKRUserEmail();
        baseEmailService.sendEmailSimple(context, userEmailArr, ProductEmailConstants.PRODUCT_SHELF_STATUS_UP_REMIND_SUBJECT, ProductEmailConstants.PRODUCT_SHELF_STATUS_UP_REMIND_THYMELEAF);
    }

    /**
     * 上架商品结束提醒
     *
     * @param productShelfIdList
     * @param isAuction          是否是竞拍类商品
     */
    @Async
    public void productShelfStatusEndRemind(List<Long> productShelfIdList, boolean isAuction) {
        List<ProductShelfDTO> productShelfDTOList = productShelfRepository.queryProductShelfInfoByIdList(productShelfIdList);
        if (null == productShelfDTOList || productShelfDTOList.size() == 0) {
            return;
        }
        if (isAuction) {
            Map<Long, BigDecimal> map = Maps.newConcurrentMap();
            List<OrderInfo> orderList = orderInfoRepository.queryOrderInfoListByIdListAndStatusList(productShelfIdList, Lists.newArrayList(OrderStatusEnum.Enable.getCode()));
            for (OrderInfo orderInfo : orderList) {
                BigDecimal price = map.get(orderInfo.getShelfId());
                if (null == price || price.compareTo(orderInfo.getPrice()) < 0) {
                    map.put(orderInfo.getShelfId(), orderInfo.getPrice());
                }
            }
            for (ProductShelfDTO productShelfDTO : productShelfDTOList) {
                productShelfDTO.setCurrentPrice(map.get(productShelfDTO.getId()));
            }
        }
        Context context = new Context();
        context.setVariable("productShelfDTOList", productShelfDTOList);
        //所有当前用户的邮箱
        String[] userEmailArr = queryAllOKRUserEmail();
        baseEmailService.sendEmailSimple(context, userEmailArr, ProductEmailConstants.PRODUCT_SHELF_STATUS_END_REMIND_SUBJECT, ProductEmailConstants.PRODUCT_SHELF_STATUS_END_REMIND_THYMELEAF);
    }


    /**
     * 上架商品即将下架提醒
     */
    @Async
    public void productShelfStatusDownPreRemind() {
        //兑换类商品 下架条件： 剩余兑换库存不足  有效结束时间小于当前时间
        ProductShelfDTO dto = new ProductShelfDTO();
        dto.setStatus(ProductShelfStatusEnum.UP.getCode());
        List<ProductShelfDTO> productShelfDTOList = productShelfRepository.queryListByDto(dto, 0, Integer.MAX_VALUE);
        if (null == productShelfDTOList || productShelfDTOList.size() == 0) {
            log.info("上架商品即将下架提醒---上架商品存在");
            return;
        }

        //可用时间 即将过期
        List<ProductShelfDTO> validDateEndPreShelfList = Lists.newArrayList();
        List<Long> shelfIdList = Lists.newArrayList();

        //获取需要下架的 商品
        for (ProductShelfDTO productShelfDTO : productShelfDTOList) {
            //当天即将结束的项目
            if (DateUtil.toStringYMD(productShelfDTO.getValidDateEnd()).equals(DateUtil.toStringYMD(new Date()))) {
                validDateEndPreShelfList.add(productShelfDTO);
                shelfIdList.add(productShelfDTO.getId());
            }
        }
        //判断是否需要邮寄提醒
        if (validDateEndPreShelfList.size() == 0) {
            log.info("上架商品即将下架提醒---无即将下架商品");
            return;
        }
        Map<Long, BigDecimal> map = Maps.newConcurrentMap();
        List<OrderInfo> orderList = orderInfoRepository.queryOrderInfoListByIdListAndStatusList(shelfIdList, Lists.newArrayList(OrderStatusEnum.Locked.getCode()));
        for (OrderInfo orderInfo : orderList) {
            BigDecimal price = map.get(orderInfo.getShelfId());
            if (null == price || price.compareTo(orderInfo.getPrice()) < 0) {
                map.put(orderInfo.getShelfId(), orderInfo.getPrice());
            }
        }
        for (ProductShelfDTO productShelfDTO : validDateEndPreShelfList) {
            productShelfDTO.setCurrentPrice(map.get(productShelfDTO.getId()));
        }
        Context context = new Context();
        context.setVariable("productShelfDTOList", validDateEndPreShelfList);
        //所有当前用户的邮箱
        String[] userEmailArr = queryAllOKRUserEmail();
        baseEmailService.sendEmailSimple(context, userEmailArr, ProductEmailConstants.PRODUCT_SHELF_STATUS_PRE_END_REMIND_SUBJECT, ProductEmailConstants.PRODUCT_SHELF_STATUS_PRE_END_REMIND_THYMELEAF);

    }


    /**
     * 上架商品 兑换/竞拍开始提醒
     */
    @Async
    public void productShelfStartSellRemind() {
        //查询上架的商品
        ProductShelfDTO dto = new ProductShelfDTO();
        dto.setStatus(ProductShelfStatusEnum.UP.getCode());
        List<ProductShelfDTO> productShelfDTOList = productShelfRepository.queryListByDto(dto, 0, Integer.MAX_VALUE);
        List<ProductShelfDTO> sellDtoList = Lists.newArrayList();
        //获取可售的项目
        for (ProductShelfDTO productShelfDTO : productShelfDTOList) {
            //当天可售的项目
            if (DateUtil.toStringYMD(productShelfDTO.getValidDateStart()).equals(DateUtil.toStringYMD(new Date()))) {
                sellDtoList.add(productShelfDTO);
            }
        }
        if (sellDtoList.size() == 0) {
            return;
        }
        Context context = new Context();
        context.setVariable("productShelfDTOList", sellDtoList);
        //所有当前用户的邮箱
        String[] userEmailArr = queryAllOKRUserEmail();
        baseEmailService.sendEmailSimple(context, userEmailArr, ProductEmailConstants.PRODUCT_SHELF_STATUS_START_SELL_REMIND_SUBJECT, ProductEmailConstants.PRODUCT_SHELF_STATUS_START_SELL_REMIND_THYMELEAF);

    }


    /**
     * 获取所有的okr用户邮箱
     *
     * @return
     */
    private String[] queryAllOKRUserEmail() {
        List<String> list = userService.queryAllOKRUserEmail();
        return list.toArray(new String[list.size()]);
    }
}

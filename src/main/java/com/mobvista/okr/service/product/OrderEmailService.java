package com.mobvista.okr.service.product;

import com.mobvista.okr.constants.ProductEmailConstants;
import com.mobvista.okr.dto.product.OrderEmailInfoDTO;
import com.mobvista.okr.enums.product.OrderStatusEnum;
import com.mobvista.okr.repository.OrderInfoRepository;
import com.mobvista.okr.service.BaseEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：订单邮件服务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/15 11:07
 */
@Service
public class OrderEmailService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(OrderEmailService.class);

    @Resource
    private BaseEmailService baseEmailService;
    @Resource
    private OrderInfoRepository orderInfoRepository;

    /**
     * 订单兑换成功提醒
     */
    @Async
    public void orderExchangeSuccessRemind(OrderEmailInfoDTO orderEmailInfoDTO) {
        Context context = new Context();
        context.setVariable("orderEmailInfoDTO", orderEmailInfoDTO);
        baseEmailService.sendEmailSimple(context, orderEmailInfoDTO.getCreateUserEmail(), ProductEmailConstants.ORDER_EXCHANGE_REMIND_SUBJECT, ProductEmailConstants.ORDER_EXCHANGE_REMIND_THYMELEAF);

    }

    /**
     * 竞拍结果 成功提醒
     */
    @Async
    public void orderAuctionSuccessRemind(List<Long> successOrderIdList) {
        if (null == successOrderIdList || successOrderIdList.size() == 0) {
            log.info("竞拍成功提醒----需要提醒的订单不存在");
            return;
        }
        List<OrderEmailInfoDTO> emailInfoDTOS = orderInfoRepository.queryOrderInfoByOrderIdList(successOrderIdList);
        Context context;
        for (OrderEmailInfoDTO dto : emailInfoDTOS) {
            context = new Context();
            context.setVariable("orderEmailInfoDTO", dto);
            baseEmailService.sendEmailSimple(context, dto.getCreateUserEmail(), ProductEmailConstants.ORDER_AUCTION_SUCCESS_REMIND_SUBJECT, ProductEmailConstants.ORDER_AUCTION_SUCCESS_REMIND_THYMELEAF);

        }
    }


    /**
     * 竞拍过程 成功提醒
     */
    @Async
    public void orderAuctionLockRemind(Long productShelfId, BigDecimal currentPrice) {
        List<OrderEmailInfoDTO> orderEmailInfoDTOList = orderInfoRepository.queryOrderInfoByShelfIdAndStatus(productShelfId, OrderStatusEnum.Locked.getCode());
        for (OrderEmailInfoDTO dto : orderEmailInfoDTOList) {
            orderAuctionUnLockRemind(dto, currentPrice, true);
        }
    }

    /**
     * 竞拍过程 释放提醒
     */
    @Async
    public void orderAuctionUnLockRemind(List<OrderEmailInfoDTO> orderEmailInfoDTOList, BigDecimal currentPrice) {
        for (OrderEmailInfoDTO dto : orderEmailInfoDTOList) {
            orderAuctionUnLockRemind(dto, currentPrice, false);
        }
    }

    /**
     * 竞拍过程 释放提醒
     *
     * @param dto
     */
    @Async
    public void orderAuctionUnLockRemind(OrderEmailInfoDTO dto, BigDecimal currentPrice, boolean success) {
        Context context = new Context();
        context.setVariable("orderEmailInfoDTO", dto);
        context.setVariable("currentPrice", currentPrice);
        context.setVariable("success", success);
        baseEmailService.sendEmailSimple(context, dto.getCreateUserEmail(), ProductEmailConstants.ORDER_AUCTION_REMIND_SUBJECT, ProductEmailConstants.ORDER_AUCTION_REMIND_THYMELEAF);

    }
}

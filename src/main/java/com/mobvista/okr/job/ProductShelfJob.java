package com.mobvista.okr.job;

import com.mobvista.okr.service.product.OrderService;
import com.mobvista.okr.service.product.ProductEmailService;
import com.mobvista.okr.service.product.ProductShelfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 描述：商品上架job
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/16 15:26
 */
@Component
public class ProductShelfJob {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ProductShelfJob.class);


    @Resource
    private OrderService orderService;
    @Resource
    private ProductShelfService productShelfService;
    @Resource
    private ProductEmailService productEmailService;

    /**
     * 商品下架订单处理JOB
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void productShelfDownOrder() {
        log.info("商品下架订单处理JOB-----开始执行");
        try {
            orderService.processAuctionProductShelfOrder();
        } catch (Exception e) {
            log.error("商品下架订单处理JOB-----异常", e);
        } finally {
            log.info("商品下架订单处理JOB-----执行结束");
        }

    }

    /**
     * 兑换类商家项目下架JOB
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void productShelfExchangeDown() {
        log.info("兑换类商家项目下架JOB-----开始执行");
        try {
            productShelfService.processExchangeProductShelfDown();
        } catch (Exception e) {
            log.error("兑换类商家项目下架JOB-----异常", e);
        } finally {
            log.info("兑换类商家项目下架JOB-----执行结束");
        }
    }


    /**
     * 自动上架项目处理
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void autoUpProductShelf() {
        log.info("自动上架项目处理----执行----开始");
        try {
            productShelfService.autoUpProductShelf();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("自动上架项目处理----执行----结束");

        }
    }

    /**
     * 上架商品即将下架提醒JOB
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void productShelfStatusDownPreRemind() {
        log.info("上架商品即将下架提醒JOB-----开始执行");
        try {
            productEmailService.productShelfStatusDownPreRemind();
        } catch (Exception e) {
            log.error("上架商品即将下架提醒JOB-----异常", e);
        } finally {
            log.info("上架商品即将下架提醒JOB-----执行结束");
        }
    }


}

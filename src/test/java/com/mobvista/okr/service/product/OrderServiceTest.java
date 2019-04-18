package com.mobvista.okr.service.product;

import com.mobvista.okr.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/19 17:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderServiceTest {

    @Resource
    private OrderService orderService;


    @Test
    public void processAuctionProductShelfOrder() {
        orderService.processAuctionProductShelfOrder();
    }
}
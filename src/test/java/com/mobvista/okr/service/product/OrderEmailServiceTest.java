package com.mobvista.okr.service.product;

import com.google.common.collect.Lists;
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
 * @date 2018/10/24 13:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class OrderEmailServiceTest {
    @Resource
    private OrderEmailService orderEmailService;

    @Test
    public void orderAuctionSuccessRemind() {
        orderEmailService.orderAuctionSuccessRemind(Lists.newArrayList(60L));
    }
}
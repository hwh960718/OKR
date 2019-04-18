package com.mobvista.okr.service.product;

import com.mobvista.okr.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/17 11:20
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ProductEmailServiceTest {

    @Resource
    private ProductEmailService productEmailService;


    @Test
    public void productShelfStatusUpRemind() {
    }

    @Test
    public void productShelfStatusEndRemind() {
    }

    @Test
    public void productShelfStatusDownPreRemind() {
    }

    @Test
    public void productShelfStartSellRemind() throws IOException {
        productEmailService.productShelfStartSellRemind();
        System.out.println("线程挂起。。。");
        System.in.read();
    }
}
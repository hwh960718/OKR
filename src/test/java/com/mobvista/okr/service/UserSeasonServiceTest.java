package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.Application;
import com.mobvista.okr.common.CommonResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/8/22 15:40
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserSeasonServiceTest {

    @Resource
    private UserSeasonService userSeasonService;

    @Test
    public void test() {
        CommonResult commonResult = userSeasonService.queryUserSeasonViews(false);
        System.out.println(JSON.toJSONString(commonResult));
    }

}
package com.mobvista.okr.service.score;

import com.mobvista.okr.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/27 15:12
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ScoreHandleServiceTest {

    @Resource
    private ScoreHandleService scoreHandleService;

    @Test
    public void handle() {
        scoreHandleService.handle(109L);
    }
}
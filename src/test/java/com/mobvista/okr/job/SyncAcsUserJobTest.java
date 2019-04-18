package com.mobvista.okr.job;

import com.mobvista.okr.Application;
import com.mobvista.okr.service.AdjusterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author 顾炜(GUWEI)
 * @date 2018/5/17 11:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class SyncAcsUserJobTest {

    @Resource
    private SyncAcsUserJob syncAcsUserJob;
    @Resource
    private AdjusterService adjusterService;

    @Test
    public void syncUser() {
        syncAcsUserJob.syncUser();
    }

    @Test
    public void test1() {
        adjusterService.importHistorySeasonAdjuster(1L);
    }

}
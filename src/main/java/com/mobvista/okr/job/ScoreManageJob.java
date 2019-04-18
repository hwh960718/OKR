package com.mobvista.okr.job;

import com.mobvista.okr.service.score.ScoreRuleService;
import com.mobvista.okr.service.score.ScoreUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 积分规则定时任务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/25 11:51
 */
@Component
public class ScoreManageJob {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ScoreManageJob.class);


    @Resource
    private ScoreRuleService scoreRuleService;
    @Resource
    private ScoreUserService scoreUserService;


    /**
     * 过期积分规则
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void expireScoreRule() {
        log.info("过期积分规则----开始");
        try {
            scoreRuleService.expiredScoreRule();
        } catch (Exception e) {
            log.error("过期积分规则----异常", e);
        }
        log.info("过期积分规则----结束");
    }

    /**
     * 启用积分规则
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void enableScoreRule() {
        log.info("启用积分规则----开始");
        try {
            scoreRuleService.enableScoreRule();
        } catch (Exception e) {
            log.error("启用积分规则----异常", e);
        }
        log.info("启用积分规则----结束");
    }

    /**
     * 同步积分用户
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void synSoreUser() {
        log.info("同步积分用户----开始");
        try {
            scoreUserService.synScoreUserRule();
        } catch (Exception e) {
            log.error("同步积分用户----异常", e);
        }
        log.info("同步积分用户----结束");
    }

}

package com.mobvista.okr.job;

import com.mobvista.okr.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/6/8 15:35
 */
@Component
public class NoticeJob {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(NoticeJob.class);


    @Resource
    private NoticeService noticeService;


    /**
     * 更改过期的公告
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void expireNotice() {
        log.info("过期已经超过有效期的公告");
        noticeService.expiredUnValidNotice();
    }


}

package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.service.StatisticsUserSeasonService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 顾炜(GUWEI) 时间：2018/5/4 15:47
 */
@RestController
@RequestMapping("/api/u/statistics")
public class StatisticsResource {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(StatisticsResource.class);


    @Resource
    private StatisticsUserSeasonService statisticsUserSeasonService;

    @ApiOperation(value = "排名TOP10的评选人的数量")
    @GetMapping("/getUserAssessCountTop10")
    public CommonResult getUserAssessCountTop10(Long seasonId) {
        log.debug("REST request to getUserAssessCountTop10 seasonId: {}", seasonId);
        return CommonResult.success(statisticsUserSeasonService.getUserAssessCountTop10(seasonId));
    }
}

package com.mobvista.okr.service;

import com.mobvista.okr.dto.StatisticsUserSeasonCountDTO;
import com.mobvista.okr.repository.StatisticsUserSeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户考评统计服务
 *
 * @author 顾炜(GUWEI) 时间：2018/5/4 15:13
 */
@Service
public class StatisticsUserSeasonService {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(StatisticsUserSeasonService.class);
    @Resource
    private StatisticsUserSeasonRepository statisticsUserSeasonRepository;

    /**
     * 排名TOP 10 的评选人的数量
     */
    public List<StatisticsUserSeasonCountDTO> getUserAssessCountTop10(Long seasonId) {
        return statisticsUserSeasonRepository.getUserAssessCountTop10(seasonId);
    }

}

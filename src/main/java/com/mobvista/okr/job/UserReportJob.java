package com.mobvista.okr.job;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mobvista.okr.domain.AssessTask;
import com.mobvista.okr.domain.Season;
import com.mobvista.okr.domain.UserReportDetail;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.dto.UserReportDetailDTO;
import com.mobvista.okr.enums.UserReportDetailStatusEnum;
import com.mobvista.okr.repository.AssessTaskRepository;
import com.mobvista.okr.repository.SeasonRepository;
import com.mobvista.okr.repository.UserReportDetailRepository;
import com.mobvista.okr.repository.UserSeasonRepository;
import com.mobvista.okr.service.score.ScoreHandleService;
import com.mobvista.okr.util.OKRListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户举报排名重新定时任务
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/31 12:50
 */
@Component
public class UserReportJob {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(UserReportJob.class);


    @Resource
    private UserReportDetailRepository userReportDetailRepository;
    @Resource
    private SeasonRepository seasonRepository;
    @Resource
    private AssessTaskRepository assessTaskRepository;
    @Resource
    private UserSeasonRepository userSeasonRepository;
    @Resource
    private MakeSeasonScoreJob makeSeasonScoreJob;
    @Resource
    private ScoreHandleService scoreHandleService;

    @Scheduled(cron = "0 0 9-22/1 * * ? ")
    @Transactional(rollbackFor = RuntimeException.class)
    public void execute() {
        log.info("用户举报排名重新定时任务-----开始执行");
        //获取所有已确认的举报
        List<UserReportDetail> userReportDetailList = userReportDetailRepository.findAllByStatus(UserReportDetailStatusEnum.SURE.getCode());
        if (null == userReportDetailList || userReportDetailList.size() == 0) {
            log.info("用户举报排名重新定时任务----执行结束----已确认的举报为空");
            return;
        }

        Map<Long, List<Long>> userReportMap = Maps.newHashMap();
        List<Long> assessTaskIdList = new ArrayList<>();
        userReportDetailList.forEach(detail -> {
            Long assessTaskId = detail.getAssessTaskId();
            assessTaskIdList.add(assessTaskId);
            List<Long> list = OKRListUtil.newArrayListIfNull(userReportMap.get(assessTaskId));
            list.add(detail.getId());
            userReportMap.put(assessTaskId, list);
        });
        Map<Long, List<Long>> seasonMap = Maps.newHashMap();
        //获取对应的season
        List<AssessTask> assessTaskList = assessTaskRepository.findByIdIn(assessTaskIdList);
        //去重
        Set<Long> userSeasonIdSet = Sets.newHashSet();
        Map<Long, List<Long>> userSeasonMap = Maps.newHashMap();
        assessTaskList.forEach(assessTask -> {
            Long userSeasonId = assessTask.getUserSeasonId();
            userSeasonIdSet.add(userSeasonId);
            List<Long> list = OKRListUtil.newArrayListIfNull(userSeasonMap.get(userSeasonId));
            list.addAll(userReportMap.get(assessTask.getId()));
            userSeasonMap.put(userSeasonId, list);
        });
        List<UserSeason> userSeasonList = userSeasonRepository.findAllByIdList(new ArrayList<>(userSeasonIdSet));
        Set<Long> seasonIdSet = Sets.newHashSet();
        userSeasonList.forEach(userSeason -> {
            Long seasonId = userSeason.getSeasonId();
            seasonIdSet.add(seasonId);
            List<Long> list = OKRListUtil.newArrayListIfNull(seasonMap.get(seasonId));
            list.addAll(userSeasonMap.get(userSeason.getId()));
            seasonMap.put(seasonId, list);
        });


        List<Long> userReportDetailIdList = Lists.newArrayList();
        List<Season> seasonList = seasonRepository.findByIdIn(new ArrayList<>(seasonIdSet));
        for (Season season : seasonList) {
            if (!season.isMakeSeasonScore()) {
                log.info("用户举报排名重新定时任务----该任务还未执行评分");
                continue;
            }
            makeSeasonScoreJob.makeSeasonScore(season);
            userReportDetailIdList.addAll(seasonMap.get(season.getId()));
        }
        if (userReportDetailIdList.size() > 0) {
            userReportDetailRepository.updateUserReportDetailStatusByIds(UserReportDetailStatusEnum.PROCESSED.getCode(), userReportDetailIdList);
        }
        handleUserScore(userReportDetailIdList);
        log.info("用户举报排名重新定时任务----执行结束");
    }


    private void handleUserScore(List<Long> userReportDetailIdList) {
        try {
            log.info("用户举报定时任务----积分处理----开始");
            List<UserReportDetailDTO> detailDTOList = userReportDetailRepository.queryByIdListIn(userReportDetailIdList);
            scoreHandleService.handleAccessReport(detailDTOList);
            log.info("用户举报定时任务----积分处理----结束");
        } catch (Exception e) {
            log.error("用户举报定时任务----积分处理----异常", e);
        }
    }
}

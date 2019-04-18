package com.mobvista.okr.job;

import com.google.common.collect.Lists;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.SeasonStatus;
import com.mobvista.okr.enums.UserRankEnum;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.service.AdjusterService;
import com.mobvista.okr.service.UserService;
import com.mobvista.okr.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时生成评价任务
 * cron = "0 0 0 * * ?"
 */
@Component
public class MakeAssessTaskJob {
    private final Logger log = LoggerFactory.getLogger(MakeAssessTaskJob.class);

    @Resource
    private SeasonRepository seasonRepository;

    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private AdjusterRepository adjusterRepository;

    @Resource
    private AssessTaskRepository assessTaskRepository;

    @Resource
    private UserProfileRepository userProfileRepository;

    @Resource
    private AdjusterService adjusterService;
    @Resource
    private UserService userService;
    /**
     * 部门对应人员id map
     */
    private Map<Long, List<Long>> departmentUserMap;

    /**
     * 对应职级其他部门需要的评价人数
     */
    private Map<String, Integer> otherDepartmentNeedCountMap;


    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = RuntimeException.class)
    public void execute() {
        log.info("定时生成评价任务开始.........");
        //统计当前各职级需要评价人的数量
        adjusterService.getAllRankAdjusterCount();
        long start = System.currentTimeMillis();
        Season queryData = new Season();
        queryData.setStatus(SeasonStatus.PUBLISHED.getCode());
        queryData.setSecondStageStartTime(new Date());
        List<Season> seasons = seasonRepository.findAllBySeason(queryData, 0, Integer.MAX_VALUE);
        if (null == seasons || seasons.size() == 0) {
            log.info("定时生成评价任务结束，需要执行的考核数据为空！");
            return;
        }
        //部门人员map
        departmentUserMap = userService.getUserIdListByDepartmentId();
        //对应职级其他部门需要的评价人数
        otherDepartmentNeedCountMap = userService.getRankOtherDepartmentNeedCount();
        for (Season season : seasons) {
            if (season.isMakeAssessTask()) {
                continue;
            }
            try {
                makeAssessTask(season);
            } catch (Exception e) {
                log.error("定时生成评价任务异常，考核数据为：{}", season, e);
            }
        }
        log.info("定时生成评价任务结束，耗时：" + DateUtil.getLogTimeByMilliseconds(System.currentTimeMillis() - start));
    }

    /**
     * 对季度生成评价任务
     *
     * @param season
     */
    public void makeAssessTask(Season season) {
        //获取用户季度考核
        //生成评价任务是
        List<UserSeason> userSeasons = userSeasonRepository.findAllBySeasonId(season.getId());
        //获取考核任务
        List<AssessTask> assessTasks = new ArrayList<>();
        //获取所有的用户信息
        List<Long> userIdList = userSeasons.stream().map(UserSeason::getUserId).collect(Collectors.toList());
        List<UserProfile> userProfileList = userProfileRepository.findAllByIdIn(userIdList);
        Map<Long, UserProfile> userProfileMap = userProfileList.stream().collect(Collectors.toMap(UserProfile::getId, u -> u));
        for (UserSeason userSeason : userSeasons) {
            //当前考核人
            Long userId = userSeason.getUserId();
            //获取当前考核人的评价人
            //排除无效离职用户
            List<Adjuster> adjusterList = adjusterRepository.findAllByUserSeasonIdExceptInvalidUser(userSeason.getId());
            if (adjusterList == null || adjusterList.size() == 0) {
                log.warn("生成用户评价任务时，评价人数量未达到要求，用户id：" + userId + "，目前评价人数量：0");
                continue;
            }

            UserProfile userProfile = userProfileMap.get(userSeason.getUserId());

            //获取当前考核人的评价人数量配置
            List<Adjuster> randomAdjusters = getRandomAdjusters(adjusterList, userProfile);

            //获取考核任务
            assessTasks.addAll(getAssessTasks(userSeason.getId(), randomAdjusters));
        }
        if (null != assessTasks && assessTasks.size() > 0) {
            assessTaskRepository.insertList(assessTasks);
        }
        //更新season状态
        season.setMakeAssessTask(true);
        seasonRepository.updateByPrimaryKeySelective(season);

    }

    /**
     * 随机获取评价人
     *
     * @param adjusterList
     * @param userProfile
     * @return
     */
    private List<Adjuster> getRandomAdjusters(List<Adjuster> adjusterList, UserProfile userProfile) {
        if (adjusterList == null || adjusterList.size() == 0) {
            return new ArrayList<>();
        }
        //当前部门id
        Long currentUserId = userProfile.getId();
        List<Long> currentDepartIdList = userService.getUserDepartmentIdFromMapByUserId(currentUserId, departmentUserMap);
        //当前部门下人员id
        List<Long> currentDepartUserIdList = userService.getUserIdListFromMapByDepartmentIdList(currentDepartIdList, departmentUserMap);
        //其他部门评价
        List<Adjuster> otherList = Lists.newArrayList();
        //当前部门评价
        List<Adjuster> currentList = Lists.newArrayList();
        for (Adjuster adjuster : adjusterList) {
            if (currentDepartUserIdList.contains(adjuster.getAdjusterId())) {
                currentList.add(adjuster);
            } else {
                otherList.add(adjuster);
            }
        }

        //获取当前用户对应的评价人数量
        int adjusterCount = getAdjusterCount(adjusterList, userProfile);
        //当前用户职级
        String currentUserRank = userProfile.getRank();
        Integer otherDepNeedCount = otherDepartmentNeedCountMap.get(UserRankEnum.getRankInfoByDetailContains(currentUserRank));

        otherDepNeedCount = null == otherDepNeedCount ? 0 : otherDepNeedCount;

        log.info("随机获取评价人----> 当前用户ID:{},当前用户:{},当前用户职级:{} 当前部门人数:{},其他部门人数:{},需要评价的人数:{},需要其他部门评价的人数:{}",
                currentUserId, userProfile.getRealName(), currentUserRank, currentList.size(), otherList.size(), adjusterCount, otherDepNeedCount);
        List<Adjuster> resultList = new ArrayList<>();
        //1、从其他部门优先获取当前职级需要的其他部门的人数
        resultList.addAll(getRandomAdjusterList(otherList, otherDepNeedCount));
        //1.1、其他部门中移除已选定的人数
        otherList.removeAll(resultList);
        //2、从本部门获取剩余的评价人数
        resultList.addAll(getRandomAdjusterList(currentList, adjusterCount - resultList.size()));
        //3、从其他部门获取剩余的评价人数
        resultList.addAll(getRandomAdjusterList(otherList, adjusterCount - resultList.size()));
        return resultList;
    }

    /**
     * 获取随机评定用户
     *
     * @param adjusterList
     * @param adjusterCount
     * @return
     */
    private List<Adjuster> getRandomAdjusterList(List<Adjuster> adjusterList, int adjusterCount) {
        //当前评价人数量若小于等于需要评价的数量是，直接返回评价人列表，无需随机抽取
        int listSize = null == adjusterList ? 0 : adjusterList.size();
        if (listSize <= 0 || adjusterCount <= 0) {
            log.info("获取随机评定用户----当前评定用户人数:{},----需要选择人数:{},----实际选择人数:0", listSize, adjusterCount);
            return new ArrayList<>();
        }

        if (listSize <= adjusterCount) {
            log.info("获取随机评定用户----当前评定用户人数:{},----需要选择人数:{},----实际选择人数:{}", listSize, adjusterCount, listSize);
            return adjusterList;
        }
        //获取指定list随机下标
        Random random = new Random();
        Set<Integer> randomList = new HashSet<>(adjusterCount);
        do {
            randomList.add(random.nextInt(listSize));
        } while (randomList.size() < adjusterCount);

        //基于用户的随机下标获取评价用户
        //随机获取评价人
        List<Adjuster> adjusters = Lists.newArrayList();
        for (Integer index : randomList) {
            adjusters.add(adjusterList.get(index));
        }
        log.info("获取随机评定用户----当前评定用户人数:{},----需要选择人数:{},----实际选择人数:{}", listSize, adjusterCount, adjusters.size());
        return adjusters;
    }

    /**
     * 获取当前考核对象需要进行评价的评价人数量
     *
     * @param adjusterList
     * @param userProfile
     * @return
     */
    private int getAdjusterCount(List<Adjuster> adjusterList, UserProfile userProfile) {

        int adjusterCount = adjusterList.size();

        //如果是T1/T2/T3级别，获取对应评价人数量
        String rank = userProfile.getRank();
        if (rank != null && rank.length() != 0) {
            adjusterCount = adjusterService.getAdjusterCountFromRedisByRankContains(rank);
            //判定是否是管理级获取其他非T1-T3职级
            adjusterCount = adjusterCount > 0 ? adjusterCount : adjusterList.size();
        }

        if (adjusterCount > adjusterList.size()) {
            adjusterCount = adjusterList.size();
        }
        log.info("获取当前考核对象需要进行评价的评价人数量：{}，用户：【userId={},userRank={}】", adjusterCount, userProfile.getId(), userProfile.getRank());
        return adjusterCount;
    }

    /**
     * 获取评价任务
     *
     * @param userSeasonId
     * @param adjusterList
     * @return
     */
    private List<AssessTask> getAssessTasks(Long userSeasonId, List<Adjuster> adjusterList) {
        if (adjusterList == null || adjusterList.size() == 0) {
            return new ArrayList<>();
        }

        List<AssessTask> assessTasks = Lists.newArrayList();
        for (Adjuster adjuster : adjusterList) {
            //
            AssessTask assessTask = new AssessTask();
            assessTask.setAssessorId(adjuster.getAdjusterId());
            assessTask.setUserSeasonId(userSeasonId);
            assessTask.setStatus(AssessTaskStatus.UNDERWAY.getCode());
            assessTasks.add(assessTask);
        }
        return assessTasks;
    }

}

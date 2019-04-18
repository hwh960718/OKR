package com.mobvista.okr.job;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mobvista.okr.constants.TaskScoreConstants;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.enums.*;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.service.AdjusterService;
import com.mobvista.okr.service.MakeScoreAndRankService;
import com.mobvista.okr.service.UserRankService;
import com.mobvista.okr.service.UserService;
import com.mobvista.okr.service.score.ScoreHandleService;
import com.mobvista.okr.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 定时生成评分任务(第二阶段结束后半小时生成季度评分)
 * cron = "0 0 1 * * ?"
 */
@Component
public class MakeSeasonScoreJob {
    private final Logger log = LoggerFactory.getLogger(MakeSeasonScoreJob.class);

    @Resource
    private SeasonRepository seasonRepository;

    @Resource
    private UserSeasonRepository userSeasonRepository;

    @Resource
    private UserProfileRepository userProfileRepository;

    @Resource
    private AssessTaskRepository assessTaskRepository;

    @Resource
    private ScoreOptionRepository scoreOptionRepository;

    @Resource
    private MakeScoreAndRankService makeScoreAndRankService;

    @Resource
    private UserSeasonItemRepository itemRepository;

    @Resource
    private AdjusterService adjusterService;
    @Resource
    private UserService userService;
    @Resource
    private UserRankService userRankService;

    /**
     * 部门对应人员id map
     */
    private Map<Long, List<Long>> departmentUserMap;

    private Map<String, Integer> rankOtherDepartmentCount;
    @Resource
    private ScoreHandleService scoreHandleService;

    public static void main(String[] args) {
        System.out.println(TaskScoreConstants.OTHER_DEPARTMENT_PERCENT);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = RuntimeException.class)
    public void execute() {
        log.info("生成评分和排名开始........");
        long start = System.currentTimeMillis();
        //待处理的季度
        Season queryData = new Season();
        queryData.setStatus(SeasonStatus.PUBLISHED.getCode());
        queryData.setMakeAssessTask(true);
        queryData.setSecondStageEndTime(new Date());
        List<Season> seasons = seasonRepository.findAllBySeason(queryData, 0, Integer.MAX_VALUE);
        if (null == seasons || seasons.size() == 0) {
            log.info("生成评分和排名结束，需要执行的考核数据为空！");
            return;
        }
        //部门人员map
        departmentUserMap = userService.getUserIdListByDepartmentId();
        //获取职级对应其他部门人员数
        rankOtherDepartmentCount = userService.getRankOtherDepartmentNeedCount();
        //获取用户职级权重map
        Map<String, Integer> userRankMap = userRankService.getUserRankMapByRedis();
        //计算评分和排名
        for (Season season : seasons) {
            if (season.isMakeSeasonScore()) {
                log.info("生成评分和排名结束,当前考核任务已经执行.{}", JSON.toJSONString(season));
                continue;
            }
            try {

                makeSeasonScore(season, userRankMap);
            } catch (Exception e) {
                log.error("生成评分和排名异常。{}", season, e);
            }

            try {
                Long seasonId = season.getId();
                log.error("生成评分和排名----积分处理----执行----开始。seasonId={}", seasonId);
                scoreHandleService.handle(seasonId);
                log.error("生成评分和排名----积分处理----执行----结束。seasonId={}", seasonId);
            } catch (Exception e) {
                log.error("生成评分和排名----积分处理----执行异常", e);
            }
        }
        log.info("生成评分和排名结束，耗时：" + DateUtil.getLogTimeByMilliseconds(System.currentTimeMillis() - start));
    }

    /**
     * 生成评分任务
     *
     * @param season
     */
    public void makeSeasonScore(Season season) {
        Map<String, Integer> userRankMap = userRankService.getUserRankMapByRedis();
        makeSeasonScore(season, userRankMap);
    }

    /**
     * 生成评分任务
     *
     * @param season
     */
    public void makeSeasonScore(Season season, Map<String, Integer> userRankMap) {
        List<UserSeason> userSeasons = userSeasonRepository.findAllBySeasonId(season.getId());
        //重新计算分数
        List<ScoreOption> optionList = scoreOptionRepository.findAllByType(null);
        Map<Long, ScoreOption> scoreOptionMap = optionList.stream().collect(Collectors.toMap(ScoreOption::getId, scoreOption -> scoreOption));
        userSeasons.forEach(userSeason -> {
            makeScoreAndRankService.makeUserSeasonScore(userSeason, scoreOptionMap, userRankMap);
        });

        //更新 用户季度考核
        updateUserSeason(userSeasons);

        //设置排名
        setUserSeasonItemRanking(userSeasons, optionList);

        //更新 待评价任务
        updateAssessTask(season.getId());

        //更新season评分状态
        season.setMakeSeasonScore(true);
        season.setStatus(SeasonStatus.FINISH.getCode());
        seasonRepository.updateByPrimaryKeySelective(season);

    }

    /**
     * 更新 用户季度考核
     *
     * @param userSeasonList
     */
    private void updateUserSeason(List<UserSeason> userSeasonList) {
        if (userSeasonList == null || userSeasonList.size() == 0) {
            return;
        }
        List<UserSeason> updateList = Lists.newArrayList();
        List<Long> userIdList = userSeasonList.stream().map(UserSeason::getUserId).collect(Collectors.toList());
        List<UserProfile> userList = userProfileRepository.findAllByIdIn(userIdList);
        Map<Long, UserProfile> userProfileMap = userList.stream().collect(Collectors.toMap(UserProfile::getId, u -> u));
        for (UserSeason userSeason : userSeasonList) {
            UserProfile userInfo = userProfileMap.get(userSeason.getUserId());
            Long userSeasonId = userSeason.getId();
            if (userInfo == null) {
                log.warn("更新用户季度考核任务时，用户信息为空，userSeasonId:" + userSeasonId);
                continue;
            }
            //先判断是否属于T1-3级别 && 判断是否达到设置的最小完成比例
//            if (belongT1_3(userInfo) && hasAssessSuccess(userSeasonId, userInfo) && assessTaskDepartmentPer(userSeason.getUserId(), userSeasonId)) {
//                userSeason.setAssessStatus(AssessStatus.SUCCESS.getCode());
//                userSeason.setAssessResult(AssessResultEnum.VALID.getCode());
//            } else {
//                userSeason.setAssessStatus(AssessStatus.FAIL.getCode());
//                userSeason.setAssessResult(AssessResultEnum.INVALID.getCode());
//            }
            //记录考核失败原因
            if (!belongT1_3(userInfo)) {
                //先判断是否属于T1-3级别
                userSeason.setAssessStatus(AssessStatus.FAIL.getCode());
                userSeason.setAssessResult(AssessResultEnum.INVALID_BELONG_T1_3.getCode());
            } else if (!hasAssessSuccess(userSeasonId, userInfo)) {
                //判断是否达到评论人设置的最小完成比例
                userSeason.setAssessStatus(AssessStatus.FAIL.getCode());
                userSeason.setAssessResult(AssessResultEnum.INVALID_ASSESS_COUNT_NEED.getCode());
            } else if (!assessTaskDepartmentPer(userSeason.getUserId(), userSeasonId)) {
                //判断其他部门评论人数条件十分满足
                userSeason.setAssessStatus(AssessStatus.FAIL.getCode());
                userSeason.setAssessResult(AssessResultEnum.INVALID_OTHER_DEPARTMENT_COUNT_NEED.getCode());
            } else {
                userSeason.setAssessStatus(AssessStatus.SUCCESS.getCode());
                userSeason.setAssessResult(AssessResultEnum.VALID.getCode());
            }

            updateList.add(userSeason);
        }
        if (updateList.size() > 0) {
            userSeasonRepository.updateList(updateList);
        }
    }

    /**
     * 判断用户其他部门评价人数是否满足
     *
     * @param userId
     * @param userSeasonId
     * @return true：满足，false：不满足
     */
    private boolean assessTaskDepartmentPer(Long userId, Long userSeasonId) {
        List<AssessTask> assessTaskList = assessTaskRepository.findAllByUserSeasonId(userSeasonId);
        if (assessTaskList == null || assessTaskList.size() == 0) {
            log.info("判断用户其他部门评价人数是否满足，当前用户：{}，评价人数为空，判定结果：false", userId);
            return false;
        }
        UserProfile userInfo = userProfileRepository.selectByPrimaryKey(userId);
        //当前部门id
        List<Long> currentDepartIdList = userService.getUserDepartmentIdFromMapByUserId(userInfo.getId(), departmentUserMap);
        //当前部门下人员id
        List<Long> currentDepartUserIdList = userService.getUserIdListFromMapByDepartmentIdList(currentDepartIdList, departmentUserMap);

        //获取评定人对应的部门
        Integer otherSize = 0;
        Integer currentSize = 0;
        //获取所有评定人的部门信息
        for (AssessTask assessTask : assessTaskList) {
            if (currentDepartUserIdList.contains(assessTask.getAssessorId())) {
                currentSize++;
            } else {
                otherSize++;
            }
        }
        Integer needCount = rankOtherDepartmentCount.get(UserRankEnum.getEnumByDetailContains(userInfo.getRank()).getInfo());
        needCount = null == needCount ? 0 : needCount;
        boolean bool = otherSize >= needCount;
        log.info("判断用户其他部门评价人数是否满足，当前用户：{}，其他部门评定人数：{}，本部门评定人数：{}，需要其他部门评价人数：{}，判定结果：{}", userId, otherSize, currentSize, needCount, bool);
        return bool;

    }

    /**
     * 设置用户评分明细排名
     *
     * @param userSeasons
     * @param optionList
     */
    private void setUserSeasonItemRanking(List<UserSeason> userSeasons, List<ScoreOption> optionList) {
        List<Long> optionIdList = new ArrayList<>();
        optionIdList.add(0L);
        for (ScoreOption option : optionList) {
            optionIdList.add(option.getId());
        }

        List<Long> userSeasonIdList = new ArrayList<>();
        for (UserSeason userSeason : userSeasons) {
            //判断用户考核是否有效
            Byte assessResult = userSeason.getAssessResult();
            if (null != assessResult && AssessResultEnum.VALID.getCode() != assessResult) {
                continue;
            }
            userSeasonIdList.add(userSeason.getId());
        }
        List<UserSeasonItem> allUserSeasonItemList = new ArrayList<>();
        if (userSeasonIdList.size() > 0) {
            allUserSeasonItemList = itemRepository.findAllByUserSeasonIdIn(userSeasonIdList);
        }

        Map<Long, List<UserSeasonItem>> optionUserSeasonItemMap = new HashMap<>();
        for (Long optionId : optionIdList) {
            List<UserSeasonItem> list = optionUserSeasonItemMap.get(optionId);
            if (list == null) {
                list = new ArrayList<>();
            }
            for (UserSeasonItem userSeasonItem : allUserSeasonItemList) {
                if (optionId.equals(userSeasonItem.getOptionId())) {
                    list.add(userSeasonItem);
                }
            }
            optionUserSeasonItemMap.put(optionId, list);
        }

        //拆分新增和更新
        List<UserSeasonItem> updateList = Lists.newArrayList();
        List<UserSeasonItem> insertList = Lists.newArrayList();
        for (Long optionId : optionIdList) {
            List<UserSeasonItem> itemList = optionUserSeasonItemMap.get(optionId);
            itemList.sort((item1, item2) -> {
                if (item1.getScore() == null) {
                    item1.setScore(BigDecimal.ZERO);
                }
                if (item2.getScore() == null) {
                    item2.setScore(BigDecimal.ZERO);
                }
                return item2.getScore().compareTo(item1.getScore());
            });
            for (int index = 0; index < itemList.size(); index++) {
                UserSeasonItem item = itemList.get(index);
                item.setRanking(index + 1);
                Date date = new Date();
                item.setCreatedDate(date);
                item.setLastModifiedDate(date);
                if (null != item.getId() && item.getId() > 0) {
                    updateList.add(item);
                } else {
                    insertList.add(item);
                }
            }
        }
        if (updateList.size() > 0) {
            itemRepository.updateList(updateList);
        }
        if (insertList.size() > 0) {
            itemRepository.insertList(insertList);
        }
    }

    /**
     * 更新 待评价任务
     * <p>
     * 注：考核阶段结束之后，如果评价任务状态为“评价中”，把评价任务状态修改成“未评价”
     * </p>
     *
     * @param seasonId
     */
    private void updateAssessTask(Long seasonId) {
        List<AssessTask> assessTaskList = assessTaskRepository.findAllBySeasonId(seasonId);

        if (assessTaskList == null || assessTaskList.size() == 0) {
            return;
        }

        List<Long> assessTaskIdList = new ArrayList<>();
        for (AssessTask assessTask : assessTaskList) {
            if (AssessTaskStatus.UNDERWAY.getCode() == assessTask.getStatus()) {
                assessTaskIdList.add(assessTask.getId());
            }
        }

        if (assessTaskIdList.size() > 0) {
            assessTaskRepository.updateStatusByIdList(AssessTaskStatus.NOT_ASSESS.getCode(), assessTaskIdList);
        }
    }

    /**
     * 判断是否达到设置的最小完成评论数
     *
     * @param userInfo
     * @param userSeasonId
     * @return
     */
    private boolean hasAssessSuccess(Long userSeasonId, UserProfile userInfo) {
        int finishedCount = assessTaskRepository.countByUserSeasonIdAndScoreIsNotNull(userSeasonId);
        if (finishedCount <= 0) {
            log.info("判断是否达到设置的最小完成评论数-->当前用户ID:{},当前用户:{},完成数据量为0 --> 判定:false", userInfo.getId(), userInfo.getRealName());
            return false;
        }

        String rank = userInfo.getRank();
        if (StringUtils.isBlank(rank)) {
            log.info("判断是否达到设置的最小完成评论数-->当前用户ID:{},当前用户:{},当前用户级别不存在 --> 判定:false", userInfo.getId(), userInfo.getRealName());
            return false;
        }

        int needCount = adjusterService.getAdjusterCountFromRedisByRankContains(rank);
        BigDecimal scale = BigDecimal.valueOf(finishedCount).divide(BigDecimal.valueOf(needCount), 2, BigDecimal.ROUND_HALF_DOWN);
        BigDecimal needScale = adjusterService.getFinishAssessMinScaleByRedisKey();
        boolean bool = scale.compareTo(needScale) >= 0;
        log.info("判断是否达到设置的最小完成评论数-->当前用户ID:{},当前用户:{},被评论数:{};[评定标准:需要评论数:{},评论完成百分比:{}]完成评价比例：{} --> 判定:{}",
                userInfo.getId(), userInfo.getRealName(), finishedCount, needCount, needScale, scale, bool);
        return bool;
    }

    /**
     * 判断是否属于T1 T2 T3 级别
     *
     * @return
     */
    private boolean belongT1_3(UserProfile userInfo) {
        String rank = userInfo.getRank();
        if (StringUtils.isBlank(rank)) {
            log.info("判断是否属于T1,T2,T3级别-->当前用户ID:{},当前用户:{},用户级别为空 --> 判定:false", userInfo.getId(), userInfo.getRealName());
            return false;
        }
        UserRankEnum userRankEnum = UserRankEnum.getEnumByDetailContains(rank);
        if (null == userRankEnum) {
            log.info("判断是否属于T1,T2,T3级别-->当前用户ID:{},当前用户:{},用户级别:{} --> 判定:false", userInfo.getId(), userInfo.getRealName(), rank);
            return false;
        }
        log.info("判断是否属于T1,T2,T3级别-->当前用户ID:{},当前用户:{},用户级别:{} --> 判定:true", userInfo.getId(), userInfo.getRealName(), rank);
        return true;
    }
}

package com.mobvista.okr.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.UserRankEnum;
import com.mobvista.okr.repository.AssessTaskItemRepository;
import com.mobvista.okr.repository.AssessTaskRepository;
import com.mobvista.okr.repository.UserProfileRepository;
import com.mobvista.okr.repository.UserSeasonItemRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 注释：生成评分和排名service
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-03-10 07:10
 */
@Service
public class MakeScoreAndRankService {

    private final Logger log = LoggerFactory.getLogger(MakeScoreAndRankService.class);

    @Resource
    private AssessTaskRepository assessTaskRepository;

    @Resource
    private AssessTaskItemRepository assessTaskItemRepository;

    @Resource
    private UserSeasonItemRepository userSeasonItemRepository;

    @Resource
    private UserProfileRepository userProfileRepository;
    @Resource
    private AdjusterService adjusterService;

    /**
     * 默认权重
     */
    private static BigDecimal defaultWeight = new BigDecimal(10);

    /**
     * 生成个人评分
     *
     * @param userSeason
     */
    @Transactional
    public void makeUserSeasonScore(UserSeason userSeason, Map<Long, ScoreOption> scoreOptionMap, Map<String, Integer> userRankMap) {

        String msg = "生成个人评分和排名，请求数据：" + JSON.toJSONString(userSeason) + "............";

        try {
            //获取评价任务
            List<AssessTask> assessTasks = assessTaskRepository.findAllByUserSeasonId(userSeason.getId());

            List<AssessTask> finishedTaskList = assessTasks.stream().filter(assessTask -> AssessTaskStatus.FINISHED.getCode() == assessTask.getStatus()).collect(Collectors.toList());

            if (finishedTaskList == null || finishedTaskList.size() == 0) {
                log.warn("【异常】未获取到已完成评价的任务............{}", msg);
                return;
            }
            //判断是否被加锁
//            if (RedisUtil.getInstance().locks().lock(lockKeyMakeScore + "_" + userSeason.getId(), lockTimeMakeScoreAcquire, lockTimeMakeScoreExpire)) {
            //生成被考核人评分
            setUserSeasonItemScore(userSeason, scoreOptionMap, finishedTaskList, userRankMap);
            log.warn("【执行完成】{}", msg);
            //            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成个人评分和排名异常，请求数据：" + JSON.toJSONString(userSeason));
        }
    }


    /**
     * 根据考核维度，计算各维度的总评分
     * key:维度编号
     * value:总评分
     * 注：不包含总评
     *
     * @param finishedTaskList       单签用户对应已完成的评价任务
     * @param rankNoMap              用户职级&职级序号 map
     * @param userRankAdjustCountMap 用户职级对应要求评价人总数map
     * @param currentUserId          当前被评价用户id
     * @return
     */
    private Map<Long, BigDecimal> getOptionTotalScoreMap(List<AssessTask> finishedTaskList, Map<String, Integer> rankNoMap, Map<String, Integer> userRankAdjustCountMap, Long currentUserId) {
        //评价人数总数
        int assessCount = finishedTaskList.size();

        List<Long> assessTaskIdList = Lists.newArrayList();
        List<Long> userIdList = Lists.newArrayList();
        Map<Long, AssessTask> assessTaskMap = Maps.newHashMap();
        finishedTaskList.forEach(at -> {
            assessTaskIdList.add(at.getId());
            userIdList.add(at.getAssessorId());
            assessTaskMap.put(at.getId(), at);
        });

        //获取用户对应的职级
        userIdList.add(currentUserId);
        Map<Long, String> userRankMap = getUserRankMap(userIdList);

        //获取当前用户职级
        String currentUserRank = userRankMap.get(currentUserId);
        //获取当前用户需要被评价的用户数量
        Integer needAssessCount = userRankAdjustCountMap.get(UserRankEnum.getRankInfoByDetailContains(currentUserRank));
        //若获取不到需要被评价的数量，则 设置：需要被评价数量=评价数量（评分规则计算公式使用）
        needAssessCount = null == needAssessCount ? assessCount : needAssessCount;

        //获取当前用户对应的职级序号
        Integer currentRankNo = rankNoMap.get(currentUserRank);
        //将用户评分项信息已评分项的维度分装数据
        Map<Long, List<AssessTaskItem>> optionIdMap = convertAssessTaskItemByOptionId(assessTaskIdList);

        Map<Long, BigDecimal> optionTotalScoreMap = Maps.newHashMap();
        for (Map.Entry<Long, List<AssessTaskItem>> map : optionIdMap.entrySet()) {
            List<AssessTaskItem> list = map.getValue();
//            总分 = 所有人累加 {[1 +（评价人级别序号 - 被评价人级别序号）*0.2]*评价人评分}/所有人累加 {[1 +（评价人级别序号 - 被评价人级别序号）*0.2]}* [1 + log10（评价人总数 / 被评价人对应要求人数）/20]
//            所有人累加{[1+（评价人级别序号-被评价人级别序号）*0.2]*评价人评分}
            BigDecimal sumRuleScore = BigDecimal.ZERO;
//            所有人累加{[1+（评价人级别序号-被评价人级别序号）*0.2]}
            BigDecimal sumRule = BigDecimal.ZERO;
//            [1+log10（评价人总数 / 被评价人对应要求人数）/20]
//            BigDecimal sum_3 = 0;
            for (AssessTaskItem ati : list) {
                Long taskId = ati.getTaskId();
                AssessTask assessTask = assessTaskMap.get(taskId);
                BigDecimal score = ati.getScore();
                //当前分不能为零
                score = null == score ? BigDecimal.ZERO : score;

                //获取职级差
                //评价人用户职级序号
                Integer assessUserRankNo = null;
                String rank = userRankMap.get(assessTask.getAssessorId());
                if (StringUtils.isNotBlank(rank)) {
                    assessUserRankNo = rankNoMap.get(rank);
                }
                int diff = 0;
                //当前用户职级序号和评价人序号均存在时，计算职级序号差，否则序号差为0
                if (null != currentRankNo && null != assessUserRankNo) {
                    diff = assessUserRankNo - currentRankNo;
                }
                //职级差不能小于0
                diff = diff > 0 ? diff : 0;

                BigDecimal decimal = new BigDecimal(1 + diff * 0.2);
                sumRuleScore = sumRuleScore.add(decimal.multiply(score));
                sumRule = sumRule.add(decimal);
            }
            optionTotalScoreMap.put(map.getKey(), calculateScoreByFormula(sumRuleScore, sumRule, assessCount, needAssessCount));

        }

        return optionTotalScoreMap;
    }

    /**
     * 根据考评维度封装map
     * key: optionId
     * value: 对应维度所有评价人的评分项
     *
     * @param assessTaskIdList
     * @return
     */
    private Map<Long, List<AssessTaskItem>> convertAssessTaskItemByOptionId(List<Long> assessTaskIdList) {
        Map<Long, List<AssessTaskItem>> optionIdMap = Maps.newHashMap();
        List<AssessTaskItem> assessTaskItemList = assessTaskItemRepository.findAllByTaskIdList(assessTaskIdList);
        assessTaskItemList.forEach(ati -> {
            List<AssessTaskItem> items = optionIdMap.get(ati.getOptionId());
            if (null == items) {
                items = Lists.newArrayList();
            }
            items.add(ati);
            optionIdMap.put(ati.getOptionId(), items);
        });
        return optionIdMap;
    }

    /**
     * 根据公式计算评分
     * sumRuleScore/sumRule*[1+log10（评价人总数 / 被评价人对应要求人数）/20]
     *
     * @param sumRuleScore    所有人累加{[1+（评价人级别序号-被评价人级别序号）*0.2]*评价人评分}
     * @param sumRule         所有人累加{[1+（评价人级别序号-被评价人级别序号）*0.2]}
     * @param assessCount
     * @param needAssessCount
     * @return
     */
    private BigDecimal calculateScoreByFormula(BigDecimal sumRuleScore, BigDecimal sumRule, int assessCount, int needAssessCount) {
        Double d = (double) assessCount / (double) needAssessCount;
        return sumRuleScore.multiply(BigDecimal.ONE.add(new BigDecimal(Math.log10(d)).divide(new BigDecimal(20)))).divide(sumRule, 2);

    }


    /**
     * 获取用户对应的职级
     *
     * @param assessUserIdList
     * @return
     */
    private Map<Long, String> getUserRankMap(List<Long> assessUserIdList) {
        List<UserProfile> userList = userProfileRepository.findAllByIdIn(assessUserIdList);
        Map<Long, String> map = Maps.newHashMap();
        for (UserProfile u : userList) {
            if (StringUtils.isNotBlank(u.getRank())) {
                map.put(u.getId(), u.getRank());
            }
        }
        return map;
    }


    /**
     * 设置被考核人评分详情
     *
     * @param userSeason
     */
    private void setUserSeasonItemScore(UserSeason userSeason, Map<Long, ScoreOption> scoreOptionMap, List<AssessTask> finishedTaskList, Map<String, Integer> rankNoMap) {
        try {
            Long userSeasonId = userSeason.getId();
            Map<String, Integer> userRankAdjustCountMap = adjusterService.getAllUserRankAdjusterCount();
            //获取评分维度对应计算规则后的分数
            Map<Long, BigDecimal> optionTotalScoreMap = getOptionTotalScoreMap(finishedTaskList, rankNoMap, userRankAdjustCountMap, userSeason.getUserId());

            //生成评分明细
            List<UserSeasonItem> userSeasonItemList = userSeasonItemRepository.findAllByUserSeasonIdIn(Lists.newArrayList(userSeasonId));
            if (userSeasonItemList == null) {
                userSeasonItemList = new ArrayList<>();
            }
            Map<Long, UserSeasonItem> userSeasonItemMap = new HashMap<>();
            if (userSeasonItemList.size() == 1) {
                Set<Map.Entry<Long, BigDecimal>> entries = optionTotalScoreMap.entrySet();
                for (Map.Entry<Long, BigDecimal> entry : entries) {
                    Long optionId = entry.getKey();
                    BigDecimal totalScore = entry.getValue();

                    UserSeasonItem userSeasonItem = new UserSeasonItem();
                    userSeasonItem.setUserSeasonId(userSeasonId);
                    userSeasonItem.setOptionId(optionId);
                    //
                    userSeasonItem.setScore(totalScore);
                    userSeasonItemMap.put(optionId, userSeasonItem);
                }
            } else {
                for (UserSeasonItem userSeasonItem : userSeasonItemList) {
                    BigDecimal score = optionTotalScoreMap.get(userSeasonItem.getOptionId());
                    if (score != null) {
//                        userSeasonItem.setScore(score.divide(count, 2, BigDecimal.ROUND_HALF_DOWN));
                        userSeasonItem.setScore(score);
                    }
                    userSeasonItemMap.put(userSeasonItem.getOptionId(), userSeasonItem);
                }
            }

            //设置综合评分
            BigDecimal totalWeight = BigDecimal.ZERO;
            for (ScoreOption scoreOption : scoreOptionMap.values()) {
                totalWeight = totalWeight.add(BigDecimal.valueOf(scoreOption.getWeight()));
            }
            BigDecimal totalScore = BigDecimal.ZERO;

            for (Map.Entry<Long, BigDecimal> optionTotalScore : optionTotalScoreMap.entrySet()) {
                Integer optionWeight = scoreOptionMap.get(optionTotalScore.getKey()).getWeight();
                BigDecimal weight = BigDecimal.valueOf(optionWeight).divide(totalWeight, 2, BigDecimal.ROUND_HALF_DOWN);
                BigDecimal score = optionTotalScore.getValue().multiply(weight);
                totalScore = totalScore.add(score);
            }

            if (null != userSeasonItemMap && userSeasonItemMap.size() > 0) {
                UserSeasonItem userSeasonItem = userSeasonItemMap.get(0L);
                if (null != userSeasonItem) {
                    userSeasonItem.setRanking(null);
                    userSeasonItem.setScore(totalScore);
                }
                List<UserSeasonItem> updateList = Lists.newArrayList();
                List<UserSeasonItem> insertList = Lists.newArrayList();
                for (Map.Entry<Long, UserSeasonItem> map : userSeasonItemMap.entrySet()) {
                    UserSeasonItem seasonItem = map.getValue();
                    if (seasonItem.getId() != null) {
                        //清空历史排名
                        seasonItem.setRanking(null);
                        updateList.add(seasonItem);
                    } else {
                        insertList.add(seasonItem);
                    }
                }
                if (updateList.size() > 0) {
                    userSeasonItemRepository.updateList(updateList);
                }
                if (insertList.size() > 0) {
                    userSeasonItemRepository.insertList(insertList);

                }
            }
        } catch (Exception e) {
            log.error("设置评分异常", e);
        } finally {
//            RedisUtil.getInstance().locks().unLock(lockKeyMakeScore + "_" + userSeasonId);
        }
    }


}

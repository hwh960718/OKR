package com.mobvista.okr.service.score;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.AssessTask;
import com.mobvista.okr.domain.ScoreRule;
import com.mobvista.okr.domain.ScoreUser;
import com.mobvista.okr.domain.ScoreUserDetail;
import com.mobvista.okr.dto.UserReportDetailDTO;
import com.mobvista.okr.dto.score.ScoreHandleDTO;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.SystemEnum;
import com.mobvista.okr.enums.score.ScoreRelatedFlagEnum;
import com.mobvista.okr.enums.score.ScoreRuleStatusEnum;
import com.mobvista.okr.enums.score.ScoreRuleTypeEnum;
import com.mobvista.okr.enums.score.ScoreTriggerModelEnum;
import com.mobvista.okr.repository.AssessTaskRepository;
import com.mobvista.okr.repository.ScoreRuleRepository;
import com.mobvista.okr.repository.ScoreUserDetailRepository;
import com.mobvista.okr.repository.ScoreUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 积分处理服务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/24 18:42
 */
@Service
public class ScoreHandleService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ScoreHandleService.class);

    @Resource
    private ScoreRuleRepository scoreRuleRepository;
    @Resource
    private AssessTaskRepository assessTaskRepository;
    @Resource
    private ScoreUserRepository scoreUserRepository;
    @Resource
    private ScoreUserDetailRepository scoreUserDetailRepository;

    /**
     * 积分处理
     *
     * @param seasonId
     */
    public void handle(Long seasonId) {
        for (ScoreTriggerModelEnum en : ScoreTriggerModelEnum.values()) {
//            if (ScoreTriggerModelEnum.Score_Exchange.equals(en)) {
//                continue;
//            }
            handle(en, seasonId);
        }
    }


    /**
     * 积分处理
     *
     * @param scoreTriggerModelEnum 触发方式
     */
    public void handle(ScoreTriggerModelEnum scoreTriggerModelEnum, Long seasonId) {
        log.info("积分处理----当前触发方式({})", scoreTriggerModelEnum.getValue());
        //获取对应触发方式的积分规则
        List<ScoreRule> ruleList = getScoreRuleByTriggerModel(scoreTriggerModelEnum);
        if (null == ruleList || ruleList.size() == 0) {
            log.info("积分处理----当前触发方式({})----对应有效积分规则为空", scoreTriggerModelEnum.getValue());
            return;
        }
        //获取当天触发方式对应的评价任务状态
        AssessTaskStatus currentStatus;
        switch (scoreTriggerModelEnum) {
            case Access_Report:
                currentStatus = AssessTaskStatus.INVALID_ASSESS;
                break;
            case Finish_Access:
                currentStatus = AssessTaskStatus.FINISHED;
                break;
            case Selected_unAccess:
                currentStatus = AssessTaskStatus.NOT_ASSESS;
                break;
            default:
                currentStatus = null;
                break;
        }
        if (null != currentStatus) {
            //评价完成
            log.info("积分处理----评价任务状态{}----(1)获取对应评价任务", currentStatus);
            List<AssessTask> assessTaskList = assessTaskRepository.findAllBySeasonIdAndStatus(seasonId, currentStatus.getCode());
            handleUserScoreByRule(ruleList, convertAssessTask2HandleDto(scoreTriggerModelEnum, assessTaskList));
        }
    }


    /**
     * 处理用户 举报确认
     *
     * @param scoreTriggerModelEnum
     * @param userId
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void handleAccessReportSure(ScoreTriggerModelEnum scoreTriggerModelEnum, Long userId, ScoreHandleDTO dto) {
        log.info("积分处理----当前触发方式({})", scoreTriggerModelEnum.getValue());
        //获取对应触发方式的积分规则
        List<ScoreRule> ruleList = getScoreRuleByTriggerModel(scoreTriggerModelEnum);
        if (null == ruleList || ruleList.size() == 0) {
            log.info("积分处理----当前触发方式({})----对应有效积分规则为空", scoreTriggerModelEnum.getValue());
            return;
        }

        List<ScoreUserDetail> scoreUserDetailList = Lists.newArrayList();
        ScoreUser scoreUser = new ScoreUser();
        scoreUser.setId(userId);
        for (ScoreRule scoreRule : ruleList) {
            Long score = Long.valueOf(scoreRule.getScore());
            //判断是否是发放
            if (ScoreRuleTypeEnum.Grant.getCode().equals(scoreRule.getType())) {
                //发放积分增加
                scoreUser.setGrantScoreTotal(addScore(scoreUser.getGrantScoreTotal(), score));
                //有效积分增加
                scoreUser.setValidScoreTotal(addScore(scoreUser.getValidScoreTotal(), score));
            } else if (ScoreRuleTypeEnum.Abatement.getCode().equals(scoreRule.getType())) {
                //有效积分减少
                scoreUser.setValidScoreTotal(subtractScore(scoreUser.getValidScoreTotal(), score));
                //消减积分增加
                scoreUser.setAbatementScoreTotal(addScore(scoreUser.getAbatementScoreTotal(), score));
            } else if (ScoreRuleTypeEnum.Penalty.getCode().equals(scoreRule.getType())) {
                //有效积分减少
                scoreUser.setValidScoreTotal(subtractScore(scoreUser.getValidScoreTotal(), score));
                //惩罚积分增加
                scoreUser.setPenaltyScoreTotal(addScore(scoreUser.getPenaltyScoreTotal(), score));
            } else if (ScoreRuleTypeEnum.Reward.getCode().equals(scoreRule.getType())) {
                //发放积分增加
                scoreUser.setGrantScoreTotal(addScore(scoreUser.getGrantScoreTotal(), score));
                //有效积分增加
                scoreUser.setValidScoreTotal(addScore(scoreUser.getValidScoreTotal(), score));
            }
            //封装用户积分明细表
            scoreUserDetailList.add(buildNewScoreUserDetail(dto, userId, scoreRule, score));
        }
        //保存用户积分信息
        saveScoreUserList(Lists.newArrayList(scoreUser));
        //保存用户积分明细
        saveScoreUserDetailList(scoreUserDetailList);

    }

    /**
     * 处理过期举报
     *
     * @param userReportDetailList
     */
    public void handleAccessReport(List<UserReportDetailDTO> userReportDetailList) {
        List<ScoreRule> ruleList = getScoreRuleByTriggerModel(ScoreTriggerModelEnum.Access_Report);
        if (null == ruleList || ruleList.size() == 0) {
            log.info("积分处理----当前触发方式(处理过期举报)----对应有效积分规则为空");
            return;
        }
        handleUserScoreByRule(ruleList, convertUserReportDetail2HandleDto(userReportDetailList));
    }


    private List<ScoreHandleDTO> convertUserReportDetail2HandleDto(List<UserReportDetailDTO> detailList) {
        List<ScoreHandleDTO> dtoList = Lists.newArrayList();
        if (null != detailList && detailList.size() > 0) {
            ScoreHandleDTO dto;
            for (UserReportDetailDTO detail : detailList) {
                dto = new ScoreHandleDTO();
                dto.setUserId(detail.getAssessTaskUserId());
                dto.setRelatedId(detail.getId());
                dto.setRelatedFlag(ScoreRelatedFlagEnum.UserReportDetail.getCode());
                dto.setRelatedDesc(ScoreTriggerModelEnum.Access_Report.getValue() + CommonConstants.APPEND_UNDER_LINE + ScoreRelatedFlagEnum.UserReportDetail.getText());
                dto.setHandleUserId(SystemEnum.User.getCode());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    private List<ScoreHandleDTO> convertAssessTask2HandleDto(ScoreTriggerModelEnum scoreTriggerModelEnum, List<AssessTask> taskList) {
        List<ScoreHandleDTO> dtoList = Lists.newArrayList();
        if (null != taskList && taskList.size() > 0) {
            ScoreHandleDTO dto;
            for (AssessTask task : taskList) {
                dto = new ScoreHandleDTO();
                dto.setUserId(task.getAssessorId());
                dto.setRelatedId(task.getId());
                dto.setRelatedFlag(ScoreRelatedFlagEnum.AssessTask.getCode());
                dto.setRelatedDesc(scoreTriggerModelEnum.getValue() + CommonConstants.APPEND_UNDER_LINE + ScoreRelatedFlagEnum.AssessTask.getText());
                dto.setHandleUserId(SystemEnum.User.getCode());
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    /**
     * 积分处理 处理评价完成、未评价任务、举报确认
     *
     * @param scoreRuleList
     */
    private void handleUserScoreByRule(List<ScoreRule> scoreRuleList, List<ScoreHandleDTO> handleDTOList) {
        //获取考核对应的完成评价任务
        boolean handleListExist = null != handleDTOList && handleDTOList.size() > 0;
        boolean ruleListExist = null != scoreRuleList && scoreRuleList.size() > 0;
        log.info("积分处理----执行逻辑---规则是否存在：{}，用户数据是否存在：{}", ruleListExist, handleListExist);
        if (handleListExist && ruleListExist) {
            List<ScoreUserDetail> scoreUserDetailList = Lists.newArrayList();
            Map<Long, ScoreUser> userMap = Maps.newHashMap();
            ScoreUser scoreUser;

            for (ScoreHandleDTO dto : handleDTOList) {
                Long userId = dto.getUserId();
                scoreUser = userMap.get(userId);
                if (null == scoreUser) {
                    scoreUser = new ScoreUser();
                }
                scoreUser.setId(userId);
                for (ScoreRule scoreRule : scoreRuleList) {
                    Long score = Long.valueOf(scoreRule.getScore());
                    //判断是否是发放
                    if (ScoreRuleTypeEnum.Grant.getCode().equals(scoreRule.getType())) {
                        //发放积分增加
                        scoreUser.setGrantScoreTotal(addScore(scoreUser.getGrantScoreTotal(), score));
                        //有效积分增加
                        scoreUser.setValidScoreTotal(addScore(scoreUser.getValidScoreTotal(), score));
                    } else if (ScoreRuleTypeEnum.Abatement.getCode().equals(scoreRule.getType())) {
                        //有效积分减少
                        scoreUser.setValidScoreTotal(subtractScore(scoreUser.getValidScoreTotal(), score));
                        //消减积分增加
                        scoreUser.setAbatementScoreTotal(addScore(scoreUser.getAbatementScoreTotal(), score));
                    } else if (ScoreRuleTypeEnum.Penalty.getCode().equals(scoreRule.getType())) {
                        //有效积分减少
                        scoreUser.setValidScoreTotal(subtractScore(scoreUser.getValidScoreTotal(), score));
                        //惩罚积分增加
                        scoreUser.setPenaltyScoreTotal(addScore(scoreUser.getPenaltyScoreTotal(), score));
                    }
                    //封装用户积分明细表
                    scoreUserDetailList.add(buildNewScoreUserDetail(dto, userId, scoreRule, score));
                }
                userMap.put(userId, scoreUser);
            }
            //保存用户积分信息
            saveScoreUserList(Lists.newArrayList(userMap.values()));
            //保存用户积分明细
            saveScoreUserDetailList(scoreUserDetailList);
        }

    }

    /**
     * @param scoreUserList
     */
    private void saveScoreUserList(List<ScoreUser> scoreUserList) {
        if (null == scoreUserList || scoreUserList.size() == 0) {
            log.info("保存用户积分----需要保存的数据为空");
            return;
        }
        scoreUserRepository.updateUserScoreList(scoreUserList);
    }

    /**
     * 保存用户积分明细
     *
     * @param scoreUserDetailList
     */
    private void saveScoreUserDetailList(List<ScoreUserDetail> scoreUserDetailList) {
        if (null == scoreUserDetailList || scoreUserDetailList.size() == 0) {
            log.info("保存用户积分明细----需要保存的数据为空");
            return;
        }
        scoreUserDetailRepository.insertList(scoreUserDetailList);

    }


    private ScoreUserDetail buildNewScoreUserDetail(ScoreHandleDTO dto, Long userId, ScoreRule scoreRule, Long score) {
        ScoreUserDetail scoreUserDetail = new ScoreUserDetail();
        scoreUserDetail.setUserId(userId);
        scoreUserDetail.setRelatedId(dto.getRelatedId());
        scoreUserDetail.setRuleId(scoreRule.getId());
        scoreUserDetail.setRelatedFlag(dto.getRelatedFlag());
        scoreUserDetail.setRelatedDesc(dto.getRelatedDesc());
        scoreUserDetail.setType(scoreRule.getType());
        scoreUserDetail.setScore(score);
        scoreUserDetail.setCreateUserId(dto.getHandleUserId());
        scoreUserDetail.setCreateDate(new Date());
        return scoreUserDetail;
    }


    /**
     * 积分增加
     *
     * @param oldScore
     * @param currentScore
     * @return
     */
    private Long addScore(Long oldScore, Long currentScore) {
        oldScore = null == oldScore ? 0L : oldScore;
        currentScore = null == currentScore ? 0L : currentScore;
        return oldScore + currentScore;
    }

    /**
     * 积分减法
     *
     * @param oldScore
     * @param currentScore
     * @return
     */
    private Long subtractScore(Long oldScore, Long currentScore) {
        oldScore = null == oldScore ? 0L : oldScore;
        currentScore = null == currentScore ? 0L : currentScore;
        return oldScore - currentScore;
    }

    /**
     * 根据触发方式获取对应的启用规则
     *
     * @param scoreTriggerModelEnum
     * @return
     */
    private List<ScoreRule> getScoreRuleByTriggerModel(ScoreTriggerModelEnum scoreTriggerModelEnum) {
        return scoreRuleRepository.findByTriggerModelAndStatus(scoreTriggerModelEnum.getCode(), ScoreRuleStatusEnum.Valid.getCode());
    }

}

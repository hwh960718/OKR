package com.mobvista.okr.service;

import com.google.common.collect.Lists;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.*;
import com.mobvista.okr.dto.UserReportDetailDTO;
import com.mobvista.okr.dto.score.ScoreHandleDTO;
import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.enums.UserReportDetailStatusEnum;
import com.mobvista.okr.enums.score.ScoreRelatedFlagEnum;
import com.mobvista.okr.enums.score.ScoreTriggerModelEnum;
import com.mobvista.okr.exception.ExceptionUtil;
import com.mobvista.okr.repository.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.score.ScoreHandleService;
import com.mobvista.okr.vm.UserReportDetailVM;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户举报服务
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 14:52
 */
@Service
public class UserReportService {

    @Resource
    private UserReportRepository userReportRepository;
    @Resource
    private AssessTaskRepository assessTaskRepository;
    @Resource
    private UserReportDetailRepository userReportDetailRepository;
    @Resource
    private SeasonRepository seasonRepository;
    @Resource
    private UserSeasonMailService userSeasonMailService;
    @Resource
    private UserSeasonRepository userSeasonRepository;
    @Resource
    private ScoreHandleService scoreHandleService;

    public Page<UserReportDetailDTO> queryList(String userName, Pageable pageable) {
        long count = userReportDetailRepository.queryCountPage(userName);

        List<UserReportDetailDTO> list = Lists.newArrayList();
        if (count > 0) {
            list = userReportDetailRepository.queryPage(userName, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }

        return new PageImpl<>(list, pageable, count);
    }


    @Transactional(rollbackFor = RuntimeException.class)
    public Long createUserReportDetail(UserReportDetailVM vm) {
        ExceptionUtil.checkState(null != vm && null != vm.getAssessTaskId(), "举报入参为空");
        ExceptionUtil.checkState(StringUtils.isNotBlank(vm.getReportContent()), "举报内容不能为空");
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(vm.getAssessTaskId());
        ExceptionUtil.checkState(null != assessTask, "评分不存在");
        UserSeason userSeason = userSeasonRepository.selectByPrimaryKey(assessTask.getUserSeasonId());
        //判断举报的考评是否结束
        Season season = seasonRepository.selectByPrimaryKey(userSeason.getSeasonId());
        ExceptionUtil.checkState(season.isMakeSeasonScore(), "考核未结束，不能提交举报");

        //当前用户id
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Long assessorId = assessTask.getAssessorId();
        //取消限制 任何人都可以评价举报
        //ExceptionUtil.checkState(currentUserId.equals(userSeason.getUserId()), "只有被评价人才能举报");
        UserReport userReport = userReportRepository.findByUserId(assessorId);
        if (null == userReport) {
            userReport = initUserReport(assessorId);
            userReportRepository.insert(userReport);
        }
        UserReportDetail detail = new UserReportDetail();
        detail.setReportId(userReport.getId());
        detail.setAssessTaskId(vm.getAssessTaskId());
        detail.setContent(vm.getReportContent());
        detail.setReportUserId(currentUserId);
        detail.setStatus(UserReportDetailStatusEnum.UN_SURE.getCode());
        detail.setCreateDate(new Date());
        UserReportDetail data = userReportDetailRepository.findByAssessTaskIdAndReportUserId(vm.getAssessTaskId(), currentUserId);
        ExceptionUtil.checkState(null == data, "您已提交举报，不能重复提交");
        userReportDetailRepository.insert(detail);
        vm.setAssessTaskUserId(assessorId);
        vm.setReportUserId(currentUserId);
        Long detailId = detail.getId();
        vm.setId(detailId);
        userSeasonMailService.assessTaskReportMailToOKRAdmin(vm);
        return detailId;
    }

    private UserReport initUserReport(Long userId) {
        UserReport userReport;
        userReport = new UserReport();
        userReport.setUserId(userId);
        userReport.setAssessCount(0);
        userReport.setReportedCount(0);
        userReport.setCreateDate(new Date());
        userReport.setLastModifyDate(new Date());
        return userReport;
    }

    /**
     * 更新被举报数
     *
     * @param reportDetailId
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateReportCount(Long reportDetailId, byte status) {
        ExceptionUtil.checkState(UserReportDetailStatusEnum.checkVerifyCode(status), "审核请求异常");
        ExceptionUtil.checkState(null != reportDetailId, "入参为空");
        UserReportDetail userReportDetail = userReportDetailRepository.selectByPrimaryKey(reportDetailId);
        ExceptionUtil.checkState(null != userReportDetail, "举报信息不存在");
        ExceptionUtil.checkState(userReportDetail.getStatus() == UserReportDetailStatusEnum.UN_SURE.getCode(), "已经审核");
        userReportDetail.setStatus(status);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        userReportDetail.setVerifyUserId(currentUserId);
        userReportDetail.setVerifyDate(new Date());
        userReportDetailRepository.updateByPrimaryKeySelective(userReportDetail);
        //将考核打分设置为无效评价
        AssessTask assessTask = assessTaskRepository.selectByPrimaryKey(userReportDetail.getAssessTaskId());
        //累计次数
        userReportRepository.updateUserReportCount(userReportDetail.getReportId());
        boolean reportStatusBool = status == UserReportDetailStatusEnum.SURE.getCode();
        if (reportStatusBool) {
            assessTask.setStatus(AssessTaskStatus.INVALID_ASSESS.getCode());
            assessTaskRepository.updateByPrimaryKeySelective(assessTask);
        }
        UserReportDetailVM vm = new UserReportDetailVM();
        vm.setStatus(status);
        vm.setStatusValue(UserReportDetailStatusEnum.getValueByCode(status));
        Long reportUserId = userReportDetail.getReportUserId();
        vm.setReportUserId(reportUserId);
        vm.setAssessTaskUserId(assessTask.getAssessorId());
        vm.setVerifyUserId(currentUserId);
        userSeasonMailService.assessTaskReportMailToReportUser(vm);
        //若审核通过，
        if (reportStatusBool) {
            //给举报用户增加举报通过奖赏积分
            ScoreHandleDTO dto = new ScoreHandleDTO();
            dto.setHandleUserId(SecurityUtils.getCurrentUserId());
            dto.setRelatedId(reportDetailId);
            dto.setRelatedFlag(ScoreRelatedFlagEnum.UserReportSure.getCode());
            dto.setRelatedDesc(ScoreRelatedFlagEnum.UserReportSure.getText());
            scoreHandleService.handleAccessReportSure(ScoreTriggerModelEnum.Access_Report_Sure, reportUserId, dto);
            //同时推送信息给评价人
            vm.setUserSeasonId(assessTask.getUserSeasonId());
            userSeasonMailService.assessTaskReportMailToAssessUser(vm);
        }


    }

    /**
     * 根据总评论数
     *
     * @param userId
     */
    public void updateAssessCount(Long userId) {
        UserReport userReport = userReportRepository.findByUserId(userId);
        if (null == userReport) {
            userReport = initUserReport(userId);
            userReport.setAssessCount(1);
            userReportRepository.insert(userReport);
        } else {
            userReportRepository.updateAssessCount(userReport.getId());
        }
    }

    /**
     * 更新选择评价数
     *
     * @param userId
     */
    public void updateAdjusterCount(Long userId, int num) {
        UserReport userReport = userReportRepository.findByUserId(userId);
        if (null == userReport) {
            userReport = initUserReport(userId);
            userReport.setAssessCount(num);
            userReportRepository.insert(userReport);
        } else {
            userReportRepository.updateAdjusterCount(userReport.getId(), num);
        }
    }


    /**
     * 获取评价数排行
     *
     * @return
     */
    public CommonResult queryAssessCountInfo() {
        return CommonResult.success(userReportRepository.queryAssessCountInfo(CommonConstants.NUM_5));
    }


    /**
     * 获取评价数排行
     *
     * @return
     */
    public CommonResult queryAdjusterCountInfo() {
        return CommonResult.success(userReportRepository.queryAdjusterCountInfo(CommonConstants.NUM_5));
    }
}


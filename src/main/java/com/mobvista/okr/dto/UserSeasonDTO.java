package com.mobvista.okr.dto;

import com.mobvista.okr.domain.Season;
import com.mobvista.okr.domain.UserSeason;
import com.mobvista.okr.enums.AssessLevel;
import com.mobvista.okr.vm.XYAxisVM;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户季度考核任务列表
 *
 * @author jiahuijie
 */
public class UserSeasonDTO {

    /**
     * 用户考核ID
     */
    private Long id;
    /**
     * 季度id
     */
    private Long seasonId;
    /**
     * 季度标题
     */
    private String seasonTitle;
    /**
     * 考核开始时间
     */
    private Date startTime;
    /**
     * 季度结束时间
     */
    private Date endTime;
    /**
     * 第一阶段起始时间
     */
    private Date firstStageStartTime;
    /**
     * 第一阶段截止时间
     */
    private Date firstStageEndTime;
    /**
     * 第二阶段起始时间
     */
    private Date secondStageStartTime;
    /**
     * 第二阶段截止时间
     */
    private Date secondStageEndTime;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 目标标题
     */
    private String okrTitle;

    /**
     * 目标详情
     */
    private String okrContent;

    /**
     * 季度得分
     */
    private BigDecimal score;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 用户职级
     */
    private String userRank;
    /**
     * 是否完成内容填写
     */
    private Boolean finishContent;

    /**
     * 评价等级
     */
    private AssessLevel assessLevel;
    /**
     * 是否评价任务完成评价
     */
    private Boolean isAssessFinished;
    /**
     * 考核状态
     */
    private byte assessStatus;
    /**
     * 考核状态文本
     */
    private String assessStatusText;

    /**
     * 考核进度状态
     */
    private Integer progressStatus;

    /**
     * 考核进度状态 名称
     */
    private String progressStatusText;

    /**
     * 选择评价人数量
     */
    private Long adjustCount;
    /**
     * 被选择评价数量
     */
    private Long adjustedCount;
    /**
     * 需要被评价数量
     */
    private Long adjustedNeedCount;
    /**
     * 评价任务评价的数量
     */
    private Long assessedTaskCount;

    /**
     * 是否开始评价任务
     */
    private boolean assessTaskIsBegin;
    /**
     * 评价任务数
     */
    private Long assessTaskCount;

    /**
     * 排名 xy视图
     */
    private XYAxisVM rankXYAxis;
    /**
     * 得分 xy视图
     */
    private XYAxisVM scoreXYAxis;

    /**
     * 考评项 xy视图
     */
    private XYAxisVM scoreOptionItemXYAxis;


    /**
     * 被举报次数
     */
    private int reportedCount = 0;

    /**
     * 评价次数
     */
    private int assessCount = 0;

    public UserSeasonDTO() {
    }

    public UserSeasonDTO(UserSeason userSeason, Season season) {
        this.setId(userSeason.getId());
        this.setSeasonId(season.getId());
        this.setAssessStatus(userSeason.getAssessStatus());
        //this.setSelectedAssessor(userSeason.getSelectedAssessor());
        this.setUserId(userSeason.getUserId());
        this.setSecondStageStartTime(season.getSecondStageStartTime());
        this.setSecondStageEndTime(season.getSecondStageEndTime());
        this.setFirstStageStartTime(season.getFirstStageStartTime());
        this.setFirstStageEndTime(season.getFirstStageEndTime());
        this.setEndTime(season.getEndTime());
        this.setStartTime(season.getStartTime());
        this.setSeasonTitle(season.getTitle());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getFirstStageStartTime() {
        return firstStageStartTime;
    }

    public void setFirstStageStartTime(Date firstStageStartTime) {
        this.firstStageStartTime = firstStageStartTime;
    }

    public Date getFirstStageEndTime() {
        return firstStageEndTime;
    }

    public void setFirstStageEndTime(Date firstStageEndTime) {
        this.firstStageEndTime = firstStageEndTime;
    }

    public Date getSecondStageStartTime() {
        return secondStageStartTime;
    }

    public void setSecondStageStartTime(Date secondStageStartTime) {
        this.secondStageStartTime = secondStageStartTime;
    }

    public Date getSecondStageEndTime() {
        return secondStageEndTime;
    }

    public void setSecondStageEndTime(Date secondStageEndTime) {
        this.secondStageEndTime = secondStageEndTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOkrTitle() {
        return okrTitle;
    }

    public void setOkrTitle(String okrTitle) {
        this.okrTitle = okrTitle;
    }

    public String getOkrContent() {
        return okrContent;
    }

    public void setOkrContent(String okrContent) {
        this.okrContent = okrContent;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }


    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public AssessLevel getAssessLevel() {
        return assessLevel;
    }

    public void setAssessLevel(AssessLevel assessLevel) {
        this.assessLevel = assessLevel;
    }

    //
    public Boolean getAssessFinished() {
        return isAssessFinished;
    }

    public void setAssessFinished(Boolean assessFinished) {
        isAssessFinished = assessFinished;
    }

    public String getProgressStatusText() {
        return progressStatusText;
    }

    public void setProgressStatusText(String progressStatusText) {
        this.progressStatusText = progressStatusText;
    }


    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }

    public byte getAssessStatus() {
        return assessStatus;
    }

    public void setAssessStatus(byte assessStatus) {
        this.assessStatus = assessStatus;
    }

    public String getAssessStatusText() {
        return assessStatusText;
    }

    public void setAssessStatusText(String assessStatusText) {
        this.assessStatusText = assessStatusText;
    }

    @Override
    public String toString() {
        return "UserSeasonDTO{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", seasonTitle='" + seasonTitle + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", firstStageStartTime=" + firstStageStartTime +
                ", firstStageEndTime=" + firstStageEndTime +
                ", secondStageStartTime=" + secondStageStartTime +
                ", secondStageEndTime=" + secondStageEndTime +
                ", userId=" + userId +
                ", okrTitle='" + okrTitle + '\'' +
                ", okrContent='" + okrContent + '\'' +
                ", score=" + score +
                ", ranking=" + ranking +
                ", assessLevel=" + assessLevel +
                ", isAssessFinished=" + isAssessFinished +
                ", assessStatus=" + assessStatus +
                ", assessStatusText='" + assessStatusText + '\'' +
                ", progressStatus=" + progressStatus +
                ", progressStatusText='" + progressStatusText + '\'' +
                '}';
    }

    public Long getAdjustCount() {
        return adjustCount;
    }

    public void setAdjustCount(Long adjustCount) {
        this.adjustCount = adjustCount;
    }

    public Long getAdjustedCount() {
        return adjustedCount;
    }

    public void setAdjustedCount(Long adjustedCount) {
        this.adjustedCount = adjustedCount;
    }

    public Long getAdjustedNeedCount() {
        return adjustedNeedCount;
    }

    public void setAdjustedNeedCount(Long adjustedNeedCount) {
        this.adjustedNeedCount = adjustedNeedCount;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public Long getAssessedTaskCount() {
        return assessedTaskCount;
    }

    public void setAssessedTaskCount(Long assessedTaskCount) {
        this.assessedTaskCount = assessedTaskCount;
    }

    public Long getAssessTaskCount() {
        return assessTaskCount;
    }

    public void setAssessTaskCount(Long assessTaskCount) {
        this.assessTaskCount = assessTaskCount;
    }

    public XYAxisVM getRankXYAxis() {
        return rankXYAxis;
    }

    public void setRankXYAxis(XYAxisVM rankXYAxis) {
        this.rankXYAxis = rankXYAxis;
    }

    public XYAxisVM getScoreXYAxis() {
        return scoreXYAxis;
    }

    public void setScoreXYAxis(XYAxisVM scoreXYAxis) {
        this.scoreXYAxis = scoreXYAxis;
    }

    public int getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(int reportedCount) {
        this.reportedCount = reportedCount;
    }

    public int getAssessCount() {
        return assessCount;
    }

    public void setAssessCount(int assessCount) {
        this.assessCount = assessCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getFinishContent() {
        return finishContent;
    }

    public void setFinishContent(Boolean finishContent) {
        this.finishContent = finishContent;
    }

    public boolean isAssessTaskIsBegin() {
        return assessTaskIsBegin;
    }

    public void setAssessTaskIsBegin(boolean assessTaskIsBegin) {
        this.assessTaskIsBegin = assessTaskIsBegin;
    }

    public XYAxisVM getScoreOptionItemXYAxis() {
        return scoreOptionItemXYAxis;
    }

    public void setScoreOptionItemXYAxis(XYAxisVM scoreOptionItemXYAxis) {
        this.scoreOptionItemXYAxis = scoreOptionItemXYAxis;
    }
}

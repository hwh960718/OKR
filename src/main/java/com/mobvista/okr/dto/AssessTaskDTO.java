package com.mobvista.okr.dto;

import com.mobvista.okr.vm.XYAxisVM;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 评价任务列表DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("评价任务列表")
public class AssessTaskDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("季度id")
    private Long seasonId;
    /**
     * 用户季度考核id
     */
    private Long userSeasonId;

    @ApiModelProperty("季度标题")
    private String seasonTitle;

    @ApiModelProperty("考核开始时间")
    private Date startTime;

    @ApiModelProperty("季度结束时间")
    private Date endTime;

    @ApiModelProperty("第一阶段起始时间")
    private Date firstStageStartTime;

    @ApiModelProperty("第一阶段截止时间")
    private Date firstStageEndTime;

    @ApiModelProperty("第二阶段起始时间")
    private Date secondStageStartTime;

    @ApiModelProperty("第二阶段截止时间")
    private Date secondStageEndTime;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userRealName;

    @ApiModelProperty("用户头像")
    private String userProfilePhoto;

    private Byte status;

    private String statusText;

    /**
     * 考核进度状态
     */
    private Integer progressStatus;

    /**
     * 考核进度状态 名称
     */
    private String progressStatusText;

    /**
     * 总分
     */
    private BigDecimal score;

    /**
     * 用户职级
     */
    private String userRank;

    /**
     * xy 视图
     */
    private XYAxisVM xyAxisVM;

    public AssessTaskDTO() {
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
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

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AssessTaskDTO{" +
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
                ", userRealName='" + userRealName + '\'' +
                ", userProfilePhoto='" + userProfilePhoto + '\'' +
                ", status=" + status +
                '}';
    }

    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getProgressStatusText() {
        return progressStatusText;
    }

    public void setProgressStatusText(String progressStatusText) {
        this.progressStatusText = progressStatusText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public XYAxisVM getXyAxisVM() {
        return xyAxisVM;
    }

    public void setXyAxisVM(XYAxisVM xyAxisVM) {
        this.xyAxisVM = xyAxisVM;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }
}

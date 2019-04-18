package com.mobvista.okr.dto;

import com.mobvista.okr.domain.Season;
import com.mobvista.okr.enums.ProgressStatusEnum;
import com.mobvista.okr.enums.SeasonStatus;
import com.mobvista.okr.util.SeasonDateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 季度考核VM
 */
@ApiModel("季度考核")
public class SeasonDTO {

    private Long id;

    /**
     * 季度标题
     */
    @ApiModelProperty("季度标题")
    private String title;

    /**
     * 考核开始时间
     */
    @ApiModelProperty("考核开始时间")
    private Date startTime;

    /**
     * 考核结束时间
     */
    @ApiModelProperty("考核结束时间")
    private Date endTime;

    /**
     * 第一阶段起始时间
     */
    @ApiModelProperty("第一阶段起始时间")
    private Date firstStageStartTime;

    /**
     * 第一阶段截止时间
     */
    @ApiModelProperty("第一阶段截止时间")
    private Date firstStageEndTime;

    /**
     * 第二阶段起始时间
     */
    @ApiModelProperty("第二阶段起始时间")
    private Date secondStageStartTime;

    /**
     * 第二阶段截止时间
     */
    @ApiModelProperty("第二阶段截止时间")
    private Date secondStageEndTime;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private Byte status;

    private String statusText;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdDate;

    /**
     * 考核进度状态
     */
    private Integer progressStatus;

    /**
     * 考核进度状态 名称
     */
    private String progressStatusText;

    public SeasonDTO() {
    }

    public SeasonDTO(Season season) {
        this.setId(season.getId());
        this.setTitle(season.getTitle());
        this.setStartTime(season.getStartTime());
        this.setEndTime(season.getEndTime());
        this.setFirstStageStartTime(season.getFirstStageStartTime());
        this.setFirstStageEndTime(season.getFirstStageEndTime());
        this.setSecondStageStartTime(season.getSecondStageStartTime());
        this.setSecondStageEndTime(season.getSecondStageEndTime());
        this.setStatus(season.getStatus());
        this.setStatusText(SeasonStatus.getValueByCode(season.getStatus()));
        this.setCreatedDate(season.getCreatedDate());
        //设置考核进度状态
        ProgressStatusEnum processStatus = SeasonDateUtil.getProcessStatus(firstStageStartTime, firstStageEndTime, secondStageStartTime, secondStageEndTime);
        this.progressStatus = processStatus.getCode();
        this.progressStatusText = processStatus.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

    @Override
    public String toString() {
        return "SeasonDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", firstStageStartTime=" + firstStageStartTime +
                ", firstStageEndTime=" + firstStageEndTime +
                ", secondStageStartTime=" + secondStageStartTime +
                ", secondStageEndTime=" + secondStageEndTime +
                ", status=" + status +
                ", createdDate=" + createdDate +
                '}';
    }


}

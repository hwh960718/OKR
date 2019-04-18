package com.mobvista.okr.vm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * 季度考核VM
 */
@ApiModel("季度考核")
public class SeasonVM {

    private Long id;

    /**
     * 季度标题
     */
    @ApiModelProperty("季度标题")
    @NotBlank
    @Size(min = 1, max = 50, message = "季度标题长度为1-50个字符")
    private String title;

    /**
     * 考核开始时间
     */
    @ApiModelProperty("考核开始时间")
    @NotNull(message = "季度标题长度不能为空")
    private Date startTime;

    /**
     * 考核结束时间
     */
    @ApiModelProperty("考核结束时间")
    @NotNull(message = "考核结束时间不能为空")
    private Date endTime;

    /**
     * 第一阶段起始时间
     */
    @ApiModelProperty("制定OKR起始时间")
    @NotNull(message = "制定OKR起始时间不能为空")
    private Date firstStageStartTime;

    /**
     * 第一阶段截止时间
     */
    @ApiModelProperty("制定OKR截止时间")
    @NotNull(message = "制定OKR截止时间不能为空")
    private Date firstStageEndTime;

    /**
     * 第二阶段起始时间
     */
    @ApiModelProperty("给同事打分起始时间")
    @NotNull(message = "给同事打分起始时间不能为空")
    private Date secondStageStartTime;

    /**
     * 第二阶段截止时间
     */
    @ApiModelProperty("给同事打分截止时间")
    @NotNull(message = "给同事打分截止时间不能为空")
    private Date secondStageEndTime;

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

    @Override
    public String toString() {
        return "SeasonVM{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", firstStageStartTime=" + firstStageStartTime +
                ", firstStageEndTime=" + firstStageEndTime +
                ", secondStageStartTime=" + secondStageStartTime +
                ", secondStageEndTime=" + secondStageEndTime +
                '}';
    }
}

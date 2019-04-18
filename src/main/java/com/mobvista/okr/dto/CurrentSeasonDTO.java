package com.mobvista.okr.dto;

import com.mobvista.okr.domain.Season;
import com.mobvista.okr.domain.UserSeason;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 当前季度考核DTO
 */
@ApiModel("当前季度考核DTO")
public class CurrentSeasonDTO {

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
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createdDate;

    /**
     * 是否填完okr
     */
    @ApiModelProperty("是否填完okr")
    private Boolean isFilledOkr;

    /**
     * 是否选完评价人
     */
    /*@ApiModelProperty("是否选完评价人")
    private Boolean isSelectedAssessor;*/

    /**
     * 是否选完评价对象
     */
    @ApiModelProperty("是否选完评价对象")
    private Boolean isSelectedUsers;


    public CurrentSeasonDTO() {
    }

    public CurrentSeasonDTO(Season season, UserSeason userSeason) {
        this.setId(season.getId());
        this.setTitle(season.getTitle());
        this.setStartTime(season.getStartTime());
        this.setEndTime(season.getEndTime());
        this.setFirstStageStartTime(season.getFirstStageStartTime());
        this.setFirstStageEndTime(season.getFirstStageEndTime());
        this.setSecondStageStartTime(season.getSecondStageStartTime());
        this.setSecondStageEndTime(season.getSecondStageEndTime());
        this.setCreatedDate(season.getCreatedDate());

//        if (userSeason != null) {
//            this.setFilledOkr(userSeason.getFilledOkr());
//            //this.setSelectedAssessor(userSeason.getSelectedAssessor());
//            this.setSelectedUsers(userSeason.getSelectedUsers());
//        } else {
//            this.setFilledOkr(false);
//            //this.setSelectedAssessor(false);
//            this.setSelectedUsers(false);
//        }
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getFilledOkr() {
        return isFilledOkr;
    }

    public void setFilledOkr(Boolean filledOkr) {
        isFilledOkr = filledOkr;
    }

    /*public Boolean getSelectedAssessor() {
        return isSelectedAssessor;
    }

    public void setSelectedAssessor(Boolean selectedAssessor) {
        isSelectedAssessor = selectedAssessor;
    }*/

    public Boolean getSelectedUsers() {
        return isSelectedUsers;
    }

    public void setSelectedUsers(Boolean selectedUsers) {
        isSelectedUsers = selectedUsers;
    }

    @Override
    public String toString() {
        return "CurrentSeasonDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", firstStageStartTime=" + firstStageStartTime +
                ", firstStageEndTime=" + firstStageEndTime +
                ", secondStageStartTime=" + secondStageStartTime +
                ", secondStageEndTime=" + secondStageEndTime +
                ", createdDate=" + createdDate +
                ", isFilledOkr=" + isFilledOkr +
                ", isSelectedUsers=" + isSelectedUsers +
                '}';
    }
}

package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class Season {
    private Long id;

    private String title;

    private Date startTime;

    private Date endTime;

    private Date firstStageStartTime;

    private Date firstStageEndTime;

    private Date secondStageStartTime;

    private Date secondStageEndTime;

    private Boolean isMakeAssessTask;

    private Boolean isMakeSeasonScore;

    private Byte status;

    private Date createdDate;

    private Date lastModifiedDate;

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

    public Boolean isMakeAssessTask() {
        return isMakeAssessTask;
    }

    public void setMakeAssessTask(Boolean makeAssessTask) {
        isMakeAssessTask = makeAssessTask;
    }

    public Boolean isMakeSeasonScore() {
        return isMakeSeasonScore;
    }

    public void setMakeSeasonScore(Boolean makeSeasonScore) {
        isMakeSeasonScore = makeSeasonScore;
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

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
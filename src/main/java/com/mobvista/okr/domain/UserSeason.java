package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class UserSeason {
    private Long id;

    private Long userId;

    private Long seasonId;

    private Byte assessStatus;

    private Byte assessResult;

    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Byte getAssessStatus() {
        return assessStatus;
    }

    public void setAssessStatus(Byte assessStatus) {
        this.assessStatus = assessStatus;
    }

    public Byte getAssessResult() {
        return assessResult;
    }

    public void setAssessResult(Byte assessResult) {
        this.assessResult = assessResult;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
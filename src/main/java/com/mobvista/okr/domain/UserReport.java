package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author guwei
 */
public class UserReport {
    private Long id;

    private Long userId;

    private Integer reportedCount;

    private Integer assessCount;

    private Integer adjusterCount;

    private Date createDate;

    private Date lastModifyDate;

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

    public Integer getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(Integer reportedCount) {
        this.reportedCount = reportedCount;
    }

    public Integer getAssessCount() {
        return assessCount;
    }

    public void setAssessCount(Integer assessCount) {
        this.assessCount = assessCount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Integer getAdjusterCount() {
        return adjusterCount;
    }

    public void setAdjusterCount(Integer adjusterCount) {
        this.adjusterCount = adjusterCount;
    }
}
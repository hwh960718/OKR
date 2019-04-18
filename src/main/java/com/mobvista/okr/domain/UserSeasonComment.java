package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class UserSeasonComment {
    private Long id;


    private Long userId;

    private Long assessorId;

    private String content;

    private Long topCount;

    private Date createdDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(Long assessorId) {
        this.assessorId = assessorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopCount() {
        return topCount;
    }

    public void setTopCount(Long topCount) {
        this.topCount = topCount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
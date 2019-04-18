package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class OkrContent {
    private Long id;

    private Long userSeasonId;

    private String okrTitle;

    private String okrContent;

    private Date createdDate;

    private Date lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
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
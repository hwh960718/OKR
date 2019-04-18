package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class Department {
    private Long id;

    private String name;

    private Integer level;

    private Long parentId;

    private Long leaderCode;

    private Date lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getLeaderCode() {
        return leaderCode;
    }

    public void setLeaderCode(Long leaderCode) {
        this.leaderCode = leaderCode;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
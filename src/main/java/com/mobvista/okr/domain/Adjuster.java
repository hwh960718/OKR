package com.mobvista.okr.domain;

/**
 * @author 顾炜[GuWei]
 */
public class Adjuster {
    private Long id;

    private Long userSeasonId;

    private Long adjusterId;

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

    public Long getAdjusterId() {
        return adjusterId;
    }

    public void setAdjusterId(Long adjusterId) {
        this.adjusterId = adjusterId;
    }
}
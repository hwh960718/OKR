package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author Weier Gu (顾炜)
 */
public class ScoreUser {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 可用积分
     */
    private Long validScoreTotal;
    /**
     * 发放总积分
     */
    private Long grantScoreTotal;
    /**
     * 消减总积分
     */
    private Long abatementScoreTotal;
    /**
     * 惩罚积分数
     */
    private Long penaltyScoreTotal;
    /**
     * 锁定积分数
     */
    private Long lockedScoreTotal;

    /**
     * 修改时间
     */
    private Date modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValidScoreTotal() {
        if (null == validScoreTotal) {
            validScoreTotal = 0L;
        }
        return validScoreTotal;
    }

    public void setValidScoreTotal(Long validScoreTotal) {
        this.validScoreTotal = validScoreTotal;
    }

    public Long getGrantScoreTotal() {
        if (null == grantScoreTotal) {
            grantScoreTotal = 0L;
        }
        return grantScoreTotal;
    }

    public void setGrantScoreTotal(Long grantScoreTotal) {
        this.grantScoreTotal = grantScoreTotal;
    }

    public Long getAbatementScoreTotal() {
        if (null == abatementScoreTotal) {
            abatementScoreTotal = 0L;
        }
        return abatementScoreTotal;
    }

    public void setAbatementScoreTotal(Long abatementScoreTotal) {
        this.abatementScoreTotal = abatementScoreTotal;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getPenaltyScoreTotal() {
        return penaltyScoreTotal;
    }

    public void setPenaltyScoreTotal(Long penaltyScoreTotal) {
        this.penaltyScoreTotal = penaltyScoreTotal;
    }

    public Long getLockedScoreTotal() {
        return lockedScoreTotal;
    }

    public void setLockedScoreTotal(Long lockedScoreTotal) {
        this.lockedScoreTotal = lockedScoreTotal;
    }
}
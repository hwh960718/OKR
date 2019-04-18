package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author guwei
 */
public class ScoreUserDetail {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 类型（1：获取积分；2：扣减积分）
     */
    private Byte type;

    /**
     * 积分
     */
    private Long score;

    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 关联Id
     */
    private Long relatedId;

    /**
     * 关联标识
     */
    private String relatedFlag;

    /**
     * 渠道描述
     */
    private String relatedDesc;

    private Long createUserId;

    private Date createDate;

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

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public String getRelatedFlag() {
        return relatedFlag;
    }

    public void setRelatedFlag(String relatedFlag) {
        this.relatedFlag = relatedFlag;
    }

    public String getRelatedDesc() {
        return relatedDesc;
    }

    public void setRelatedDesc(String relatedDesc) {
        this.relatedDesc = relatedDesc;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
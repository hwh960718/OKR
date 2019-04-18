package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author guwei
 */
public class ScoreRule {
    /**
     * 规则ID
     */
    private Long id;

    private String ruleName;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 类型（1：获取积分；2：扣减积分）
     */
    private Byte type;

    /**
     * 积分数
     */
    private Integer score;

    /**
     * 状态(1：有效；2：无效)
     */
    private Byte status;

    /**
     * 有效时间
     */
    private Date validDateStart;

    /**
     * 渠道
     */
    private Byte triggerMode;

    private Long createUserId;

    private Date createDate;

    private Long modifyUserId;

    private Date modifyDate;

    /**
     * 有效结束时间
     */
    private Date validDateEnd;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDesc() {
        return ruleDesc;
    }

    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(Byte triggerMode) {
        this.triggerMode = triggerMode;
    }

    public Date getValidDateStart() {
        return validDateStart;
    }

    public void setValidDateStart(Date validDateStart) {
        this.validDateStart = validDateStart;
    }

    public Date getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(Date validDateEnd) {
        this.validDateEnd = validDateEnd;
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

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

}
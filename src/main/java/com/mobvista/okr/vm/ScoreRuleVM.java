package com.mobvista.okr.vm;

import com.mobvista.okr.util.DateUtil;

import java.util.Date;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 11:06
 */
public class ScoreRuleVM {

    private Long id;

    private String ruleName;

    private String ruleDesc;

    private Byte type;

    private Integer score;

    private Date validDateStart;

    private Date validDateEnd;

    /**
     * 有效类型 0无效，1 永久
     */
    private byte validType = 0;

    private Byte status;

    private Byte triggerMode;


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
        if (null != validDateStart) {
            validDateStart = DateUtil.setDateStart(validDateStart);
        }
        return validDateStart;
    }

    public void setValidDateStart(Date validDateStart) {
        this.validDateStart = validDateStart;
    }

    public Date getValidDateEnd() {
        if (null != validDateEnd) {
            validDateEnd = DateUtil.setDateEnd(validDateEnd);
        }
        return validDateEnd;
    }

    public void setValidDateEnd(Date validDateEnd) {
        this.validDateEnd = validDateEnd;
    }

    public byte getValidType() {
        return validType;
    }

    public void setValidType(byte validType) {
        this.validType = validType;
    }
}

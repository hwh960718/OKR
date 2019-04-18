package com.mobvista.okr.dto.score;

import com.mobvista.okr.enums.score.ScoreRuleTypeEnum;

import java.util.Date;

public class ScoreUserDetailDTO {
    private Long id;

    private Long userId;

    private String userName;

    private Byte type;

    private String typeText;

    private Long score;

    private Long ruleId;

    private Long relatedId;

    private String relatedFlag;

    private String relatedDesc;

    private Long createUserId;

    private Date createDate;

    private String assessorProfilePhoto;

    public String getAssessorProfilePhoto() {
        return assessorProfilePhoto;
    }

    public void setAssessorProfilePhoto(String assessorProfilePhoto) {
        this.assessorProfilePhoto = assessorProfilePhoto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getTypeText() {
        if (null != type) {
            typeText = ScoreRuleTypeEnum.getValueByCode(type);
        }
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }
}
package com.mobvista.okr.dto.score;

import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.enums.score.ScoreRuleStatusEnum;
import com.mobvista.okr.enums.score.ScoreRuleTypeEnum;
import com.mobvista.okr.enums.score.ScoreTriggerModelEnum;
import com.mobvista.okr.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 10:57
 */
public class ScoreRuleDTO {

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

    private String typeText;

    /**
     * 积分数
     */
    private Integer score;

    /**
     * 积分数显示
     */
    private String scoreText;

    /**
     * 状态(1：有效；2：无效)
     */
    private Byte status;

    private String statusText;
    /**
     * 有效时间
     */
    private Date validDateStart;

    /**
     * 渠道
     */
    private Byte triggerMode;

    private String triggerModeText;

    private Long createUserId;

    private Date createDate;

    private Long modifyUserId;

    private Date modifyDate;

    /**
     * 有效结束时间
     */
    private Date validDateEnd;


    private String validDateText;


    private String modifyUserName;


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

    public String getTypeText() {
        if (null != type) {
            typeText = ScoreRuleTypeEnum.getValueByCode(type);
        }
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
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

    public String getStatusText() {
        if (null != status) {
            statusText = ScoreRuleStatusEnum.getValueByCode(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Byte getTriggerMode() {
        return triggerMode;
    }

    public void setTriggerMode(Byte triggerMode) {
        this.triggerMode = triggerMode;
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

    public String getTriggerModeText() {
        if (null != triggerMode) {
            triggerModeText = ScoreTriggerModelEnum.getValueByCode(triggerMode);
        }
        return triggerModeText;
    }

    public void setTriggerModeText(String triggerModeText) {
        this.triggerModeText = triggerModeText;
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

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
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

    public String getValidDateText() {
        if (null != validDateStart) {
            validDateText = DateUtil.toStringYMD(validDateStart);
        }
        if (null != validDateEnd) {
            if (StringUtils.isNotBlank(validDateText)) {
                validDateText += CommonConstants.APPEND_ZHI;
            }
            validDateText += DateUtil.toStringYMD(validDateEnd);
        }
        return validDateText;
    }

    public void setValidDateText(String validDateText) {
        this.validDateText = validDateText;
    }

    public String getScoreText() {
        if (type != null) {
            scoreText = ScoreRuleTypeEnum.getNumberFlagByCode(type) + score;
        }
        return scoreText;
    }

    public void setScoreText(String scoreText) {
        this.scoreText = scoreText;
    }
}

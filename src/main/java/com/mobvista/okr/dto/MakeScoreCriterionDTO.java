package com.mobvista.okr.dto;

import java.math.BigDecimal;

/**
 * 评分提示
 *
 * @author Weier Gu (顾炜)
 * @date 2018/8/24 14:09
 */
public class MakeScoreCriterionDTO {

    /**
     * 得分
     */
    private BigDecimal score;
    /**
     * 用户评分等级信息
     */
    private String scoreLevelMsg;
    /**
     * 标准
     */
    private String criterion;
    /**
     * 提示
     */
    private String ps;


    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getScoreLevelMsg() {
        return scoreLevelMsg;
    }

    public void setScoreLevelMsg(String scoreLevelMsg) {
        this.scoreLevelMsg = scoreLevelMsg;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }
}

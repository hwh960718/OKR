package com.mobvista.okr.dto;

import java.io.Serializable;

/**
 * 用户统计
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/5 11:33
 */
public class UserSeasonCountDTO implements Serializable {

    private static final long serialVersionUID = -4516727789848077108L;
    /**
     * 参与OKR考核总人数
     */
    private Long okrUserCount;

    /**
     * 获取未完成OKR的员工数
     */
    private Long unFinishFilledCount;

    /**
     * OKR总评价数
     */
    private Long okrAssessCount;

    /**
     * 获取未完成评价任务的员工数
     */
    private Long unFinishAssessCount;

    public Long getOkrUserCount() {
        return okrUserCount;
    }

    public void setOkrUserCount(Long okrUserCount) {
        this.okrUserCount = okrUserCount;
    }

    public Long getUnFinishFilledCount() {
        return unFinishFilledCount;
    }

    public void setUnFinishFilledCount(Long unFinishFilledCount) {
        this.unFinishFilledCount = unFinishFilledCount;
    }

    public Long getOkrAssessCount() {
        return okrAssessCount;
    }

    public void setOkrAssessCount(Long okrAssessCount) {
        this.okrAssessCount = okrAssessCount;
    }

    public Long getUnFinishAssessCount() {
        return unFinishAssessCount;
    }

    public void setUnFinishAssessCount(Long unFinishAssessCount) {
        this.unFinishAssessCount = unFinishAssessCount;
    }
}

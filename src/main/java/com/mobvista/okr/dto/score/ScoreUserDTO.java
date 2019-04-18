package com.mobvista.okr.dto.score;

import java.util.Date;

/**
 * @author Weier Gu (顾炜)
 */
public class ScoreUserDTO {
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门名称
     */
    private String departmentName;
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
     * 锁定积分数
     */
    private Long lockedScoreTotal;

    /**
     * 修改时间
     */
    private Date modifyDate;

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

    public Long getValidScoreTotal() {
        return validScoreTotal;
    }

    public void setValidScoreTotal(Long validScoreTotal) {
        this.validScoreTotal = validScoreTotal;
    }

    public Long getGrantScoreTotal() {
        return grantScoreTotal;
    }

    public void setGrantScoreTotal(Long grantScoreTotal) {
        this.grantScoreTotal = grantScoreTotal;
    }

    public Long getAbatementScoreTotal() {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getLockedScoreTotal() {
        return lockedScoreTotal;
    }

    public void setLockedScoreTotal(Long lockedScoreTotal) {
        this.lockedScoreTotal = lockedScoreTotal;
    }
}
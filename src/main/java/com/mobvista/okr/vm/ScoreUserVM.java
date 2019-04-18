package com.mobvista.okr.vm;

import java.util.Date;

/**
 * @author Weier Gu (顾炜)
 */
public class ScoreUserVM {
    private Long id;

    private String userName;

    private Long validScoreTotal;

    private Long grantScoreTotal;

    private Long abatementScoreTotal;

    private Date modifyDate;

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
}
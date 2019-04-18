package com.mobvista.okr.dto;

import com.mobvista.okr.enums.UserReportDetailStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户举报表明细
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 14:43
 */
public class UserReportDetailDTO implements Serializable {
    private static final long serialVersionUID = -6455648112041705279L;

    private Long id;

    /**
     * 季度考核标题
     */
    private String seasonTitle;

    /**
     * 举报总表id
     */
    private Long reportId;

    /**
     * 评价任务id
     */
    private Long assessTaskId;

    private Long assessTaskUserId;


    private String assessTaskUserName;

    /**
     * 举报人id
     */
    private Long reportUserId;

    private String reportUserName;

    /**
     * 确认人id
     */
    private Long verifyUserId;

    private String verifyUserName;

    /**
     * 状态 1：已确认 2：未确认
     */
    private byte status;

    private String statusValue;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 确认时间
     */
    private Date verifyDate;

    /**
     * 举报原因
     */
    private String reportContent;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getAssessTaskId() {
        return assessTaskId;
    }

    public void setAssessTaskId(Long assessTaskId) {
        this.assessTaskId = assessTaskId;
    }

    public Long getReportUserId() {
        return reportUserId;
    }

    public void setReportUserId(Long reportUserId) {
        this.reportUserId = reportUserId;
    }

    public Long getVerifyUserId() {
        return verifyUserId;
    }

    public void setVerifyUserId(Long verifyUserId) {
        this.verifyUserId = verifyUserId;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getAssessTaskUserName() {
        return assessTaskUserName;
    }

    public void setAssessTaskUserName(String assessTaskUserName) {
        this.assessTaskUserName = assessTaskUserName;
    }

    public String getReportUserName() {
        return reportUserName;
    }

    public void setReportUserName(String reportUserName) {
        this.reportUserName = reportUserName;
    }

    public Long getAssessTaskUserId() {
        return assessTaskUserId;
    }

    public void setAssessTaskUserId(Long assessTaskUserId) {
        this.assessTaskUserId = assessTaskUserId;
    }

    public String getVerifyUserName() {
        return verifyUserName;
    }

    public void setVerifyUserName(String verifyUserName) {
        this.verifyUserName = verifyUserName;
    }

    public String getStatusValue() {
        if (status > 0) {
            statusValue = UserReportDetailStatusEnum.getValueByCode(status);
        }
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }

    @Override
    public String toString() {
        return "UserReportDetailDTO{" +
                "id=" + id +
                ", reportId=" + reportId +
                ", assessTaskId=" + assessTaskId +
                ", assessTaskUserId=" + assessTaskUserId +
                ", assessTaskUserName='" + assessTaskUserName + '\'' +
                ", reportUserId=" + reportUserId +
                ", reportUserName='" + reportUserName + '\'' +
                ", verifyUserId=" + verifyUserId +
                ", verifyUserName='" + verifyUserName + '\'' +
                ", status=" + status +
                ", statusValue='" + statusValue + '\'' +
                ", createDate=" + createDate +
                ", verifyDate=" + verifyDate +
                ", reportContent='" + reportContent + '\'' +
                '}';
    }
}

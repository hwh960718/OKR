package com.mobvista.okr.vm;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户举报表明细
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 14:43
 */
public class UserReportDetailVM implements Serializable {
    private static final long serialVersionUID = -6455648112041705279L;

    private Long id;

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
    /**
     * 确认人
     */
    private String verifyUserName;

    /**
     * 状态 1：审核中 2：审核确认 3：审核拒绝 4:已处理
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

    private Long userSeasonId;

    private String seasonTitle;

    private String sendEmail;

    private int page;

    private int size;

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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }

    public String getSeasonTitle() {
        return seasonTitle;
    }

    public void setSeasonTitle(String seasonTitle) {
        this.seasonTitle = seasonTitle;
    }
}

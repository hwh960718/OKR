package com.mobvista.okr.vm;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/10 15:28
 */
public class UserSeasonRadarVM extends RadarVM {

    /**
     * 是否可以举报 默认不能举报
     */
    private boolean canReport = false;

    /**
     * 用户举报状态
     */
    private Byte userReportStatus;

    /**
     * 用户举报状态明细
     */
    private String userReportStatusText;

    /**
     * 被举报人的 评价任务id
     */
    private Long assessTaskId;

    public Byte getUserReportStatus() {
        return userReportStatus;
    }

    public void setUserReportStatus(Byte userReportStatus) {
        this.userReportStatus = userReportStatus;
    }

    public String getUserReportStatusText() {
        return userReportStatusText;
    }

    public void setUserReportStatusText(String userReportStatusText) {
        this.userReportStatusText = userReportStatusText;
    }

    public boolean isCanReport() {
        return canReport;
    }

    public void setCanReport(boolean canReport) {
        this.canReport = canReport;
    }

    public Long getAssessTaskId() {
        return assessTaskId;
    }

    public void setAssessTaskId(Long assessTaskId) {
        this.assessTaskId = assessTaskId;
    }
}

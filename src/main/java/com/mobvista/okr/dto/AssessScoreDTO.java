package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 评价得分列表DTO 季度考核详情页评价列表
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("评价得分列表")
public class AssessScoreDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long assessorId;

    @ApiModelProperty("用户姓名")
    private String assessorRealName;

    @ApiModelProperty("用户头像")
    private String assessorProfilePhoto;

    @ApiModelProperty("总分")
    private BigDecimal score;

    private Byte status;

    /**
     * 是否举报考评任务
     */
    private boolean reportAssessTask = false;

    /**
     * 举报状态
     */
    private Byte reportStatus;
    /**
     * 举报状态明细
     */
    private String reportStatusValue;

    public AssessScoreDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssessorId() {
        return assessorId;
    }

    public void setAssessorId(Long assessorId) {
        this.assessorId = assessorId;
    }

    public String getAssessorRealName() {
        return assessorRealName;
    }

    public void setAssessorRealName(String assessorRealName) {
        this.assessorRealName = assessorRealName;
    }

    public String getAssessorProfilePhoto() {
        return assessorProfilePhoto;
    }

    public void setAssessorProfilePhoto(String assessorProfilePhoto) {
        this.assessorProfilePhoto = assessorProfilePhoto;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Byte reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportStatusValue() {
        return reportStatusValue;
    }

    public void setReportStatusValue(String reportStatusValue) {
        this.reportStatusValue = reportStatusValue;
    }

    public boolean isReportAssessTask() {
        return reportAssessTask;
    }

    public void setReportAssessTask(boolean reportAssessTask) {
        this.reportAssessTask = reportAssessTask;
    }

    @Override
    public String toString() {
        return "AssessScoreDTO{" +
                "id=" + id +
                ", assessorId=" + assessorId +
                ", assessorRealName='" + assessorRealName + '\'' +
                ", assessorProfilePhoto='" + assessorProfilePhoto + '\'' +
                ", score=" + score +
                ", status=" + status +
                '}';
    }
}

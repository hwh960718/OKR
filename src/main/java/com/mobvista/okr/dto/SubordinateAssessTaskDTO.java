package com.mobvista.okr.dto;

import com.mobvista.okr.enums.AssessTaskStatus;
import com.mobvista.okr.vm.XYAxisVM;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 下属评价任务列表DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("下属评价任务列表")
public class SubordinateAssessTaskDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("季度id")
    private Long seasonId;

    @ApiModelProperty("用户考核id")
    private Long userSeasonId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户姓名")
    private String userRealName;

    @ApiModelProperty("用户头像")
    private String userProfilePhoto;

    @ApiModelProperty("用户部门")
    private String userDepartmentName;

    @ApiModelProperty("评价人id")
    private Long assessorId;

    @ApiModelProperty("评价人用户姓名")
    private String assessorRealName;

    @ApiModelProperty("评价人用户头像")
    private String assessorProfilePhoto;

    @ApiModelProperty("评价人部门")
    private String assessorDepartmentName;

    private Byte status;

    private String statusText;

    @ApiModelProperty("总分")
    private BigDecimal score;

    private XYAxisVM xyAxisVM;

    public SubordinateAssessTaskDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Long seasonId) {
        this.seasonId = seasonId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public String getUserProfilePhoto() {
        return userProfilePhoto;
    }

    public void setUserProfilePhoto(String userProfilePhoto) {
        this.userProfilePhoto = userProfilePhoto;
    }

    public String getUserDepartmentName() {
        return userDepartmentName;
    }

    public void setUserDepartmentName(String userDepartmentName) {
        this.userDepartmentName = userDepartmentName;
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

    public String getAssessorDepartmentName() {
        return assessorDepartmentName;
    }

    public void setAssessorDepartmentName(String assessorDepartmentName) {
        this.assessorDepartmentName = assessorDepartmentName;
    }


    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getStatusText() {
        if (null != status) {
            statusText = AssessTaskStatus.getValueByCode(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public Byte getStatus() {
        return status;
    }

    public XYAxisVM getXyAxisVM() {
        return xyAxisVM;
    }

    public void setXyAxisVM(XYAxisVM xyAxisVM) {
        this.xyAxisVM = xyAxisVM;
    }

    @Override
    public String toString() {
        return "SubordinateAssessTaskDTO{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", userId=" + userId +
                ", userRealName='" + userRealName + '\'' +
                ", userProfilePhoto='" + userProfilePhoto + '\'' +
                ", userDepartmentName='" + userDepartmentName + '\'' +
                ", assessorId=" + assessorId +
                ", assessorRealName='" + assessorRealName + '\'' +
                ", assessorProfilePhoto='" + assessorProfilePhoto + '\'' +
                ", assessorDepartmentName='" + assessorDepartmentName + '\'' +
                ", status=" + status +
                ", score=" + score +
                '}';
    }


}

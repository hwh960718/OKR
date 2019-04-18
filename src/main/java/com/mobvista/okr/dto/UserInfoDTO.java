package com.mobvista.okr.dto;

import com.mobvista.okr.enums.GenderEnum;
import com.mobvista.okr.enums.UserStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户信息DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户信息")
public class UserInfoDTO {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("头像")
    private String profilePhoto;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("部门id")
    private Long departmentId;

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("职位名称")
    private String jobName;

    @ApiModelProperty("上级汇报人id")
    private Long leaderId;

    @ApiModelProperty("上级汇报人名称")
    private String leaderName;

    @ApiModelProperty("性别")
    private Byte gender;

    private String genderText;

    @ApiModelProperty("职级")
    private String rank;

    @ApiModelProperty("用户状态")
    private Byte status;
    /**
     * 用户状态描述
     */
    private String statusText;

    @ApiModelProperty("是否被选择过")
    private Boolean isSelected;

    /**
     * 被举报次数
     */
    @ApiModelProperty("被举报次数")
    private int reportedCount = 0;

    /**
     * 评价次数
     */
    @ApiModelProperty("评价次数")
    private int assessCount = 0;

    /**
     * 是否可以修改
     */
    private boolean canModify;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getReportedCount() {
        return reportedCount;
    }

    public void setReportedCount(int reportedCount) {
        this.reportedCount = reportedCount;
    }

    public int getAssessCount() {
        return assessCount;
    }

    public void setAssessCount(int assessCount) {
        this.assessCount = assessCount;
    }

    public String getGenderText() {
        if (null != gender) {
            genderText = GenderEnum.getValueByCode(gender);
        }
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public String getStatusText() {
        if (null != status) {
            statusText = UserStatus.getUserStatusValueByCode(status);
        }
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }
}

package com.mobvista.okr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户信息lite版DTO
 *
 * @author jiahuijie
 * @date 2017/12/30
 */
@ApiModel("用户信息lite版")
public class UserInfoLiteDTO {

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

    @ApiModelProperty("职级")
    private String rank;

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

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "UserInfoLiteDTO{" +
                "id=" + id +
                ", realName='" + realName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", rank=" + rank +
                '}';
    }

}

package com.mobvista.okr.domain;

import java.util.Date;

/**
 * @author 顾炜[GuWei]
 */
public class UserProfile {
    private Long id;

    private String userName;

    private String realName;

    private Long departmentId;

    private Long leaderId;

    private String email;

    private String jobName;

    private String profilePhoto;

    private String workplace;

    private String rank;

    private Byte gender;

    private String college;

    private String major;

    private String degree;

    private Byte status;

    private Date entryTime;

    private Date leavingTime;

    private Date lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(Date leavingTime) {
        this.leavingTime = leavingTime;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", departmentId=" + departmentId +
                ", leaderId=" + leaderId +
                ", email='" + email + '\'' +
                ", jobName='" + jobName + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", workplace='" + workplace + '\'' +
                ", rank='" + rank + '\'' +
                ", gender=" + gender +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", degree='" + degree + '\'' +
                ", status=" + status +
                ", entryTime=" + entryTime +
                ", leavingTime=" + leavingTime +
                ", lastModifiedDate=" + lastModifiedDate +
                '}';
    }
}
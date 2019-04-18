package com.mobvista.okr.dto.mbacs;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 注释：
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-08 下午4:44
 */
public class AccountUserProfile {

    private long id;
    //登录用户名
    private String username;
    //真实用户名
    private String realName;
    private String email;
    private byte status;
    private String userType;
    private String department;
    private Object departmentDetail;
    private String job;
    private Object jobDetail;
    private String directLeader;
    private Object directLeaderDetail;
    private String departmentLeader;
    private Object departmentLeaderDetail;
    private long entryTime;
    private long leavingTime;
    private long createdAt;
    private long updatedAt;
    private boolean accredit;

    private List<Attribute> attribute;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JSONField(name = "real_name")
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @JSONField(name = "user_type")
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @JSONField(name = "department_detail")
    public Object getDepartmentDetail() {
        return departmentDetail;
    }

    public void setDepartmentDetail(Object departmentDetail) {
        this.departmentDetail = departmentDetail;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @JSONField(name = "job_detail")
    public Object getJobDetail() {
        return jobDetail;
    }

    public void setJobDetail(Object jobDetail) {
        this.jobDetail = jobDetail;
    }

    @JSONField(name = "direct_leader")
    public String getDirectLeader() {
        return directLeader;
    }

    public void setDirectLeader(String directLeader) {
        this.directLeader = directLeader;
    }

    @JSONField(name = "direct_leader_detail")
    public Object getDirectLeaderDetail() {
        return directLeaderDetail;
    }

    public void setDirectLeaderDetail(Object directLeaderDetail) {
        this.directLeaderDetail = directLeaderDetail;
    }

    @JSONField(name = "department_leader")
    public String getDepartmentLeader() {
        return departmentLeader;
    }

    public void setDepartmentLeader(String departmentLeader) {
        this.departmentLeader = departmentLeader;
    }

    @JSONField(name = "department_leader_detail")
    public Object getDepartmentLeaderDetail() {
        return departmentLeaderDetail;
    }

    public void setDepartmentLeaderDetail(Object departmentLeaderDetail) {
        this.departmentLeaderDetail = departmentLeaderDetail;
    }

    @JSONField(name = "entry_time")
    public long getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(long entryTime) {
        this.entryTime = entryTime;
    }

    @JSONField(name = "leaving_time")
    public long getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(long leavingTime) {
        this.leavingTime = leavingTime;
    }

    @JSONField(name = "created_at")
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @JSONField(name = "updated_at")
    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isAccredit() {
        return accredit;
    }

    public void setAccredit(boolean accredit) {
        this.accredit = accredit;
    }

    public List<Attribute> getAttribute() {
        return attribute;
    }

    public void setAttribute(List<Attribute> attribute) {
        this.attribute = attribute;
    }

    /**
     * 用户属性
     */
    private static class Attribute {

    }

}

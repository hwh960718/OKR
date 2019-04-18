package com.mobvista.okr.dto;

/**
 * 用户考核&用户
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/27 13:46
 */
public class SeasonUserDTO {


    /**
     * 用户考核id
     */
    private Long userSeasonId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 职级
     */
    private String rank;
    /**
     * 性别
     */
    private Byte gender;
    /**
     * 部门名称
     */
    private String departmentName;

    public Long getUserSeasonId() {
        return userSeasonId;
    }

    public void setUserSeasonId(Long userSeasonId) {
        this.userSeasonId = userSeasonId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }
}

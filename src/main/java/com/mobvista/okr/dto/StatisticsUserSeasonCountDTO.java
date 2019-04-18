package com.mobvista.okr.dto;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 统计之 用户考评
 *
 * @author 顾炜(GUWEI) 时间：2018/5/4 15:40
 */
public class StatisticsUserSeasonCountDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String userName;
    /**
     * 用户职级
     */
    private String userRank;
    /**
     * 考评用户数
     */
    private int assessCount;
    /**
     * 考评用户
     */
    private String assessUsers;
    /**
     * 考评用户集合
     */
    private List<String> assessUserList;

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

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public int getAssessCount() {
        return assessCount;
    }

    public void setAssessCount(int assessCount) {
        this.assessCount = assessCount;
    }

    public String getAssessUsers() {
        return assessUsers;
    }

    public void setAssessUsers(String assessUsers) {
        this.assessUsers = assessUsers;
    }

    public List<String> getAssessUserList() {
        if (StringUtils.isNotBlank(assessUsers)) {
            assessUserList = Arrays.asList(assessUsers.split(","));
        }
        return assessUserList;
    }

    public void setAssessUserList(List<String> assessUserList) {
        this.assessUserList = assessUserList;
    }

    @Override
    public String toString() {
        return "StatisticsUserSeasonCountDTO{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userRank='" + userRank + '\'' +
                ", assessCount=" + assessCount +
                ", assessUsers='" + assessUsers + '\'' +
                ", assessUserList=" + assessUserList +
                '}';
    }
}

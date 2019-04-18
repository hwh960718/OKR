package com.mobvista.okr.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户职级
 *
 * @author 顾炜(GUWEI) 时间：2018/5/2 11:14
 */
public enum UserRankEnum {
    T1("T1", "T1-1,T1-2,T1-3", 4, 1),
    T2("T2", "T2-1,T2-2,T2-3", 6, 2),
    T3("T3", "T3-1,T3-2,T3-3", 8, 3);

    /**
     * 职级等级
     */
    private String info;

    /**
     * 等级详情
     */
    private String detail;
    /**
     * 评价数量（默认）
     */
    private int adjusterCount;

    private int otherDepartmentCount;

    UserRankEnum(String info, String detail, int adjusterCount, int otherDepartmentCount) {
        this.info = info;
        this.detail = detail;
        this.adjusterCount = adjusterCount;
        this.otherDepartmentCount = otherDepartmentCount;
    }

    public String getInfo() {
        return info;
    }


    public String getDetail() {
        return detail;
    }


    public int getAdjusterCount() {
        return adjusterCount;
    }

    /**
     * 根据详细等级获取对应枚举
     *
     * @param rank
     * @return
     */
    public static UserRankEnum getEnumByDetailContains(String rank) {
        if (StringUtils.isNotBlank(rank)) {
            for (UserRankEnum userRankEnum : UserRankEnum.values()) {
                if (userRankEnum.getDetail().contains(rank)) {
                    return userRankEnum;
                }
            }
        }
        return null;
    }


    public static String getRankInfoByDetailContains(String rank) {
        if (StringUtils.isNotBlank(rank)) {
            for (UserRankEnum userRankEnum : UserRankEnum.values()) {
                if (userRankEnum.getDetail().contains(rank)) {
                    return userRankEnum.getInfo();
                }
            }
        }
        return null;
    }

    /**
     * 根据归属职级获取对接枚举
     *
     * @param info
     * @return
     */
    public static UserRankEnum getEnumByInfo(String info) {
        for (UserRankEnum userRankEnum : UserRankEnum.values()) {
            if (userRankEnum.getInfo().equals(info)) {
                return userRankEnum;
            }
        }
        return null;
    }

    public int getOtherDepartmentCount() {
        return otherDepartmentCount;
    }
}

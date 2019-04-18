package com.mobvista.okr.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 注释：账号中心用户状态
 * 作者：刘腾飞【liutengfei】
 * 时间：2018-03-30 13:51
 */
public enum AcsUserStatus {

    //10为正常，9为锁定，0为失效，1为未激活
    NORMAL(10), LOCK(9), INVALID(0), NOT_ACTIVE(1);

    private static Map<Integer, AcsUserStatus> map = new HashMap<>();

    static {
        for (AcsUserStatus item : AcsUserStatus.values()) {
            map.put(item.getValue(), item);
        }
    }

    private Integer value;

    AcsUserStatus(Integer value) {
        this.value = value;
    }

    public static AcsUserStatus ofValue(Integer value) {
        return map.get(value);
    }

    @JsonValue
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

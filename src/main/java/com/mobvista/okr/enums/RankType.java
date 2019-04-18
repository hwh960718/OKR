package com.mobvista.okr.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 职级
 *
 * @author jiahuijie
 * @date 2018/1/19
 */
public enum RankType {
    INTERN1("一级实习生"),
    INTERN2("二级实习生"),
    T11("T1-1"),
    T12("T1-2"),
    T13("T1-3"),
    T21("T2-1"),
    T22("T2-2"),
    T23("T2-3"),
    T31("T3-1"),
    T32("T3-2"),
    T33("T3-3"),
    T4("T4"),
    T5("T5"),
    M1A("M1-a"),
    M1B("M1-b"),
    M1C("M1-c"),
    M2A("M2-a"),
    M2B("M2-b"),
    M2C("M2-c"),
    M3A("M3-a"),
    M3B("M3-b"),
    M3C("M3-c"),
    M4A("M4-a"),
    M4B("M4-b"),
    M4C("M4-c"),
    GM2("GM2");

    private static Map<String, RankType> map = new HashMap<>();

    static {
        for (RankType item : RankType.values()) {
            map.put(item.getValue(), item);
        }
    }

    private String value;

    RankType(String value) {
        this.value = value;
    }

    public static RankType ofValue(String value) {
        return map.get(value);
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

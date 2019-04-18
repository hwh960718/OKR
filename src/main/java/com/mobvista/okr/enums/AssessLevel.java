package com.mobvista.okr.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weier Gu (顾炜)
 */

public enum AssessLevel {

    S("S"),
    A("A"),
    B("B"),
    C("C");

    private static Map<String, AssessLevel> map = new HashMap<>();

    static {
        for (AssessLevel item : AssessLevel.values()) {
            map.put(item.getValue(), item);
        }
    }

    private String value;

    AssessLevel(String value) {
        this.value = value;
    }

    public static AssessLevel ofValue(String value) {
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

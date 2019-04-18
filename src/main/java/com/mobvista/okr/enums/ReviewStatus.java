package com.mobvista.okr.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum ReviewStatus {

    NEW("新建"),
    PASS("审核通过"),
    REFUSE("审核失败");

    private static Map<String, ReviewStatus> map = new HashMap<>();

    static {
        for (ReviewStatus item : ReviewStatus.values()) {
            map.put(item.getValue(), item);
        }
    }

    private String value;

    ReviewStatus(String value) {
        this.value = value;
    }

    public static ReviewStatus ofValue(String value) {
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

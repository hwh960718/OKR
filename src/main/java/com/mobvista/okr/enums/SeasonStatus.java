package com.mobvista.okr.enums;

/**
 * @author Weier Gu (顾炜)
 */

public enum SeasonStatus {

    NEW((byte) 1, "新建"),
    PUBLISHED((byte) 2, "已发布"),
    FINISH((byte) 3, "已结束");

    private byte code;

    private String value;

    SeasonStatus(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }


    public static String getValueByCode(byte code) {
        for (SeasonStatus s : SeasonStatus.values()) {
            if (s.getCode() == code) {
                return s.getValue();
            }
        }
        return null;
    }
}

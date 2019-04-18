package com.mobvista.okr.enums;

/**
 * 性别(男:1;女:2;未知:3;)
 */
public enum GenderEnum {
    Man((byte) 1, "男"),
    Woman((byte) 2, "女"),
    UnKnow((byte) 3, "未知");

    GenderEnum(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    private byte code;

    private String value;


    public byte getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


    public static String getValueByCode(byte code) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (code == genderEnum.getCode()) {
                return genderEnum.getValue();
            }
        }
        return UnKnow.getValue();
    }
}

package com.mobvista.okr.enums;

/**
 * 状态:(正常：NORMAL,1;未激活：NOT_ACTIVE,2;禁用：FORBIDDEN,3;失效：INVALID,4;
 *
 * @author 顾炜[GuWei]
 */
public enum UserStatus {

    NORMAL((byte) 1, "正常"),
    NO_ACTIVATED((byte) 2, "未激活"),
    FORBIDDEN((byte) 3, "禁用"),
    INVALID((byte) 4, "离职");


    private Byte code;
    private String value;

    UserStatus(Byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public Byte getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }

    public static String getUserStatusValueByCode(Byte code) {
        if (null == code) {
            return null;
        }
        for (UserStatus us : UserStatus.values()) {
            if (us.getCode().equals(code)) {
                return us.getValue();
            }
        }
        return null;
    }
}

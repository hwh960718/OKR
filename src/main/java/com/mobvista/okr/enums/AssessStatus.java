package com.mobvista.okr.enums;

/**
 * 考评状态
 *
 * @author jiahuijie
 * @date 2018/1/19
 */
public enum AssessStatus {

    /**
     * 进行中
     */
    UNDERWAY((byte) 1, "进行中"),
    /**
     * 考核失败
     */
    FAIL((byte) 2, "考核失败"),
    /**
     * 考核完成
     */
    SUCCESS((byte) 3, "考核完成");

    private byte code;

    private String value;

    AssessStatus(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }


    public static boolean check(byte code) {
        for (AssessStatus as : AssessStatus.values()) {
            if (as.getCode() == code) {
                return true;
            }
        }
        return false;
    }


    public static String getValueByCode(byte code) {
        for (AssessStatus as : AssessStatus.values()) {
            if (as.getCode() == code) {
                return as.value;
            }
        }
        return UNDERWAY.getValue();
    }

    public static Byte getCodeByName(String name) {
        for (AssessStatus as : AssessStatus.values()) {
            if (as.name().equals(name)) {
                return as.code;
            }
        }
        return null;
    }
}

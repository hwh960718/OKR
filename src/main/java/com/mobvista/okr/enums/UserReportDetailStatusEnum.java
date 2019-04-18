package com.mobvista.okr.enums;

/**
 * 1：已确认 2：未确认
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/30 15:03
 */
public enum UserReportDetailStatusEnum {
    UN_SURE((byte) 1, "处理中"),
    SURE((byte) 2, "已确认"),
    REFUSE((byte) 3, "已拒绝"),
    PROCESSED((byte) 4, "已处理");

    private byte code;

    private String value;


    UserReportDetailStatusEnum(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }

    public static String getValueByCode(Byte code) {
        for (UserReportDetailStatusEnum en : UserReportDetailStatusEnum.values()) {
            if (en.getCode() == code) {
                return en.getValue();
            }
        }
        return null;
    }

    /**
     * 校验审核状态
     *
     * @param code
     * @return
     */
    public static boolean checkVerifyCode(byte code) {
        if (code == SURE.code || code == REFUSE.code) {
            return true;
        }
        return false;
    }
}

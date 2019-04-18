package com.mobvista.okr.enums;

/**
 * 评论任务状态
 *
 * @author jiahuijie
 * @date 2018/1/19
 */
public enum AssessTaskStatus {

    /**
     * 进行中
     */
    UNDERWAY((byte) 1, "进行中"),
    /**
     * 已完成
     */
    FINISHED((byte) 2, "已完成"),
    /**
     * 未评价
     */
    NOT_ASSESS((byte) 3, "未评价"),
    /**
     * 无效评价
     */
    INVALID_ASSESS((byte) 4, "无效评价");

    private byte code;

    private String value;

    AssessTaskStatus(byte code, String value) {
        this.code = code;
        this.value = value;
    }

    public byte getCode() {
        return code;
    }


    public String getValue() {
        return value;
    }

    /**
     * 判断code 是否有效
     *
     * @param code
     * @return
     */
    public static boolean codeIsValid(Byte code) {
        if (null == code) {
            return false;
        }
        if (UNDERWAY.getCode() == code || FINISHED.getCode() == code) {
            return true;
        }

        return false;
    }

    public static String getValueByCode(byte code) {
        for (AssessTaskStatus ats : AssessTaskStatus.values()) {
            if (ats.getCode() == code) {
                return ats.getValue();
            }
        }
        return null;
    }
}

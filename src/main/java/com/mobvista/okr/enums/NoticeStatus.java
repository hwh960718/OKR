package com.mobvista.okr.enums;

/**
 * 公告状态
 * 有效:1;无效:2;删除:3
 *
 * @author jiahuijie
 * @date 2018/1/19
 */
public enum NoticeStatus {

    /**
     * 有效
     */
    Valid((byte) 1, "有效"),
    /**
     * 已无效
     */
    UnValid((byte) 2, "已无效"),
    /**
     * 已删除
     */
    Delete((byte) 3, "已删除");

    private byte code;

    private String value;

    NoticeStatus(byte code, String value) {
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
        for (NoticeStatus noticeStatus : NoticeStatus.values()) {
            if (code == noticeStatus.getCode()) {
                return noticeStatus.getValue();
            }
        }
        return null;
    }
}

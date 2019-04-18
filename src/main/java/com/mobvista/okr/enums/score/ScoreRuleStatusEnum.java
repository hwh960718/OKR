package com.mobvista.okr.enums.score;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 14:29
 */
public enum ScoreRuleStatusEnum {
    Valid((byte) 1, "有效"), Invalid((byte) 2, "无效");

    private byte code;

    private String value;

    ScoreRuleStatusEnum(byte code, String value) {
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
     * 判断code是否存在
     *
     * @param code
     * @return
     */
    public static boolean codeExists(Byte code) {
        if (null == code) {
            return false;
        }
        for (ScoreRuleStatusEnum srse : ScoreRuleStatusEnum.values()) {
            if (srse.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更加code获取对应的值
     *
     * @param code
     * @return
     */
    public static String getValueByCode(Byte code) {
        if (null != code) {
            for (ScoreRuleStatusEnum srse : ScoreRuleStatusEnum.values()) {
                if (srse.getCode() == code) {
                    return srse.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }
}

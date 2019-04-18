package com.mobvista.okr.enums.score;

import org.apache.commons.lang3.StringUtils;

/**
 * 积分类型 1：发放 2：消减
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/24 18:46
 */
public enum ScoreRuleTypeEnum {
    //发放
    Grant((byte) 1, "发放"),
    //惩罚
    Penalty((byte) 2, "惩罚"),
    //消减
    Abatement((byte) 3, "消减"),
    //奖赏
    Reward((byte) 4, "奖赏");

    private Byte code;

    private String text;


    ScoreRuleTypeEnum(Byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public Byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }


    public static ScoreRuleTypeEnum getEnumByCode(Byte code) {
        if (null != code) {
            for (ScoreRuleTypeEnum en : ScoreRuleTypeEnum.values()) {
                if (en.getCode().equals(code)) {
                    return en;
                }
            }
        }
        return Grant;
    }

    public static String getValueByCode(Byte code) {
        if (null != code) {
            for (ScoreRuleTypeEnum en : ScoreRuleTypeEnum.values()) {
                if (en.getCode().equals(code)) {
                    return en.getText();
                }
            }
        }
        return null;
    }


    /**
     * 根据code获取数字对应的标识
     *
     * @param code
     * @return 正数：“” 负数“-”
     */
    public static String getNumberFlagByCode(Byte code) {
        if (null == code) {
            return StringUtils.EMPTY;
        }

        if (code.equals(Penalty.getCode()) || code.equals(Abatement.getCode())) {
            return "-";
        }

        return StringUtils.EMPTY;
    }
}

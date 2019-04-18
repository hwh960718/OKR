package com.mobvista.okr.enums;

/**
 * 系统枚举
 *
 * @author Weier Gu (顾炜)
 * @date 2018/7/25 15:03
 */
public enum SystemEnum {
    User(-1L, "系统");

    private Long code;

    private String text;

    SystemEnum(Long code, String text) {
        this.code = code;
        this.text = text;
    }

    public Long getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

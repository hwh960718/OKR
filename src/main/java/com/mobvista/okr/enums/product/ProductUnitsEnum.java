package com.mobvista.okr.enums.product;

/**
 * 描述：商品计量单位
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/14 14:22
 */
public enum ProductUnitsEnum {
    //积分
    Score((byte) 1, "积分");

    private byte code;

    private String text;

    ProductUnitsEnum(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

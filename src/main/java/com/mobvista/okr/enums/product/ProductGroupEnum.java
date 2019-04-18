package com.mobvista.okr.enums.product;

/**
 * 商品是否组合枚举
 * 是否组合商品 1：是 2：否
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/13 17:05
 */
public enum ProductGroupEnum {
    //是
    YES((byte) 1, "是"),
    //否
    NO((byte) 2, "否");

    private byte code;

    private String text;

    ProductGroupEnum(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

    /**
     * 检查是否是组合商品
     *
     * @param code
     * @return
     */
    public static boolean checkIsGroup(Byte code) {
        if (null == code) {
            return false;
        }
        return YES.getCode() == code;
    }
}

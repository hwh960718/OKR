package com.mobvista.okr.enums.product;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 14:29
 */
public enum ProductStatusEnum {
    //启用
    Enable((byte) 1, "启用"),
    //禁用
    Disable((byte) 2, "禁用"),
    //删除
    Delete((byte) 3, "删除");

    private byte code;

    private String value;

    ProductStatusEnum(byte code, String value) {
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
        for (ProductStatusEnum srse : ProductStatusEnum.values()) {
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
            for (ProductStatusEnum srse : ProductStatusEnum.values()) {
                if (srse.getCode() == code) {
                    return srse.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }
}

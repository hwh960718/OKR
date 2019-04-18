package com.mobvista.okr.enums.product;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 14:29
 */
public enum ProductShelfStatusEnum {
    //上架
    UP((byte) 1, "上架"),
    //下架
    DOWN((byte) 2, "下架"),
    //已结束
    END((byte) 3, "已结束");

    private byte code;

    private String value;

    ProductShelfStatusEnum(byte code, String value) {
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
        for (ProductShelfStatusEnum srse : ProductShelfStatusEnum.values()) {
            if (srse.getCode() == code) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据code获取对应的值
     *
     * @param code
     * @return
     */
    public static String getValueByCode(Byte code) {
        if (null != code) {
            for (ProductShelfStatusEnum srse : ProductShelfStatusEnum.values()) {
                if (srse.getCode() == code) {
                    return srse.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }


    /**
     * 判断是否是上架
     *
     * @param code
     * @return
     */
    public static boolean isUp(Byte code) {
        if (null != code && UP.getCode() == code) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是下架
     *
     * @param code
     * @return
     */
    public static boolean isDown(Byte code) {
        if (null != code && DOWN.getCode() == code) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否结束
     *
     * @param code
     * @return
     */
    public static boolean isEnd(Byte code) {
        if (null != code && END.getCode() == code) {
            return true;
        }
        return false;
    }
}

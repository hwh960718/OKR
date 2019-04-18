package com.mobvista.okr.enums.product;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/7/23 14:29
 */
public enum OrderStatusEnum {
    //有效
    Enable((byte) 1, "完成"),
    //禁用
    Disable((byte) 2, "无效"),
    //竞拍锁定
    Locked((byte) 3, "进行中"),
    //竞拍释放
    UnLocked((byte) 4, "竞拍释放"),
    //已处理
    Processed((byte) 5, "已处理");


    private byte code;

    private String value;

    OrderStatusEnum(byte code, String value) {
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
        for (OrderStatusEnum srse : OrderStatusEnum.values()) {
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
            for (OrderStatusEnum srse : OrderStatusEnum.values()) {
                if (srse.getCode() == code) {
                    return srse.getValue();
                }
            }
        }
        return StringUtils.EMPTY;
    }
}

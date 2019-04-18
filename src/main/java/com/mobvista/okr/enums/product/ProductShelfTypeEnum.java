package com.mobvista.okr.enums.product;

/**
 * 商品类型
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/11 14:01
 */
public enum ProductShelfTypeEnum {
    //兑换
    EXCHANGE((byte) 1, "兑换"),
    //竞拍
    AUCTION((byte) 2, "竞拍");

    private byte code;

    private String text;

    ProductShelfTypeEnum(byte code, String text) {
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
     * 根据code获取描述信息
     *
     * @param code
     * @return
     */
    public static final String getTextByCode(Byte code) {
        if (null != code) {
            for (ProductShelfTypeEnum productShelfTypeEnum : ProductShelfTypeEnum.values()) {
                if (code.equals(productShelfTypeEnum.getCode())) {
                    return productShelfTypeEnum.getText();
                }
            }
        }
        return null;
    }
}

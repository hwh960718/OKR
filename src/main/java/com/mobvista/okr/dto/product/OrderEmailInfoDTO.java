package com.mobvista.okr.dto.product;

import com.mobvista.okr.enums.product.ProductShelfTypeEnum;

import java.math.BigDecimal;

/**
 * 描述：订单邮件信息
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/17 14:17
 */
public class OrderEmailInfoDTO {
    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 上架id
     */
    private Long shelfId;

    /**
     * 上架类型 1、兑换 3、竞拍
     */
    private Byte shelfType;

    private String shelfTypeText;

    /**
     * 订单状态 1、有效 2、无效
     */
    private Byte status;

    /**
     * 积分数
     */
    private BigDecimal price;
    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 订单数量
     */
    private Integer orderNum;

    /**
     * 创建人id
     */
    private Long createUserId;
    /**
     * 创建人姓名
     */
    private String createUserName;
    /**
     * 创建人邮箱
     */
    private String createUserEmail;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    public Byte getShelfType() {
        return shelfType;
    }

    public void setShelfType(Byte shelfType) {
        this.shelfType = shelfType;
    }

    public String getShelfTypeText() {
        if (shelfType != null) {
            shelfTypeText = ProductShelfTypeEnum.getTextByCode(shelfType);
        }
        return shelfTypeText;
    }

    public void setShelfTypeText(String shelfTypeText) {
        this.shelfTypeText = shelfTypeText;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserEmail() {
        return createUserEmail;
    }

    public void setCreateUserEmail(String createUserEmail) {
        this.createUserEmail = createUserEmail;
    }
}

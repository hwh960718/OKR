package com.mobvista.okr.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * order_info
 *
 * @author
 */
public class OrderInfo implements Serializable {
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单id
     */
    private Long productId;

    /**
     * 上架id
     */
    private Long shelfId;

    /**
     * 上架类型 1、兑换 3、竞拍
     */
    private Byte shelfType;

    /**
     * 订单状态 1、有效 2、无效
     */
    private Byte status;

    /**
     * 积分数
     */
    private BigDecimal price;

    /**
     * 订单数量
     */
    private Integer orderNum;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private Long modifyUserId;
    /**
     * 修改时间
     */
    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
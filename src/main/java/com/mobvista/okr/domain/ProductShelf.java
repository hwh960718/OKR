package com.mobvista.okr.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * product_shelf
 *
 * @author
 */
public class ProductShelf implements Serializable {
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 上架类型：1、兑换 2、竞拍
     */
    private Byte type;

    /**
     * 状态 ：1、上架 2、下架
     */
    private Byte status;

    /**
     * 价值
     */
    private BigDecimal price;

    /**
     * 积分增加幅度
     */
    private BigDecimal scoreIncrement;

    /**
     * 计量单位：1、积分
     */
    private Byte units;

    /**
     * 上架数量
     */
    private Integer shelfCount;
    /**
     * 上架使用数量
     */
    private Integer shelfUseCount;

    /**
     * 上架开始时间
     */
    private Date validDateStart;

    /**
     * 下架结束时间
     */
    private Date validDateEnd;

    private Long createUserId;

    private Date createDate;

    private Long modifyUserId;

    private Date modifyDate;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
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

    public BigDecimal getScoreIncrement() {
        return scoreIncrement;
    }

    public void setScoreIncrement(BigDecimal scoreIncrement) {
        this.scoreIncrement = scoreIncrement;
    }

    public Byte getUnits() {
        return units;
    }

    public void setUnits(Byte units) {
        this.units = units;
    }

    public Integer getShelfCount() {
        return shelfCount;
    }

    public void setShelfCount(Integer shelfCount) {
        this.shelfCount = shelfCount;
    }

    public Integer getShelfUseCount() {
        return shelfUseCount;
    }

    public void setShelfUseCount(Integer shelfUseCount) {
        this.shelfUseCount = shelfUseCount;
    }

    public Date getValidDateStart() {
        return validDateStart;
    }

    public void setValidDateStart(Date validDateStart) {
        this.validDateStart = validDateStart;
    }

    public Date getValidDateEnd() {
        return validDateEnd;
    }

    public void setValidDateEnd(Date validDateEnd) {
        this.validDateEnd = validDateEnd;
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
package com.mobvista.okr.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * product
 *
 * @author
 */
public class Product implements Serializable {
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 库存总数
     */
    private Integer inventoryCount;

    /**
     * 商品可用库存
     */
    private Integer validCount;

    /**
     * 使用统计
     */
    private Integer usedCount;

    /**
     * 锁定库存
     */
    private Integer lockedCount;

    /**
     * 状态 ：1、上架 2、下架
     */
    private Byte status;

    /**
     * 是否组合商品 1：是 2：否
     */
    private Byte isGroup;

    /**
     * 商品金额
     */
    private BigDecimal price;

    /**
     * 图片上传地址 ;分隔
     */
    private String picturePath;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(Integer inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getLockedCount() {
        return lockedCount;
    }

    public void setLockedCount(Integer lockedCount) {
        this.lockedCount = lockedCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Byte isGroup) {
        this.isGroup = isGroup;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
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
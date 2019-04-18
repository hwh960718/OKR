package com.mobvista.okr.dto.product;

import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.enums.product.ProductShelfTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * product_putaway
 *
 * @author
 */
public class ProductShelfDTO implements Serializable {
    private Long id;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 上架类型：1、兑换 2、竞拍
     */
    private Byte type;

    private String typeText;

    /**
     * 状态 ：1、上架 2、下架
     */
    private Byte status;

    private String statusText;

    /**
     * 商品描述
     */
    private String productDesc;

    /**
     * 价值
     */
    private BigDecimal price;
    /**
     * 当前价格
     */
    private BigDecimal currentPrice;
    /**
     * 商品价值
     */
    private BigDecimal productPrice;

    /**
     * 上架数量
     */
    private Integer shelfCount;

    /**
     * 上架使用数量
     */
    private Integer shelfUseCount;

    /**
     * 上架可用数量
     */
    private Integer shelfValidCount;

    /**
     * 积分增加幅度
     */
    private BigDecimal scoreIncrement;

    /**
     * 计量单位：1、积分
     */
    private Byte units;

    /**
     * 图片上传地址 ;分隔
     */
    private String picturePath;

    /**
     * 亚马逊图片返回
     */
    private List<AWSResponseDTO> picturePathList;


    /**
     * 上架开始时间
     */
    private Date validDateStart;

    /**
     * 下架结束时间
     */
    private Date validDateEnd;

    /**
     * 服务器当前时间
     */
    private Long systemTimeMillis;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getShelfCount() {
        return shelfCount;
    }

    public void setShelfCount(Integer shelfCount) {
        this.shelfCount = shelfCount;
    }

    public String getTypeText() {
        if (type != null) {
            typeText = ProductShelfTypeEnum.getTextByCode(type);
        }
        return typeText;
    }

    public void setTypeText(String typeText) {
        this.typeText = typeText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public List<AWSResponseDTO> getPicturePathList() {
        return picturePathList;
    }

    public void setPicturePathList(List<AWSResponseDTO> picturePathList) {
        this.picturePathList = picturePathList;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getShelfUseCount() {
        return shelfUseCount;
    }

    public void setShelfUseCount(Integer shelfUseCount) {
        this.shelfUseCount = shelfUseCount;
    }

    public Integer getShelfValidCount() {
        if (null == shelfValidCount) {
            shelfCount = null == shelfCount ? CommonConstants.NUM_0 : shelfCount;
            shelfUseCount = null == shelfUseCount ? CommonConstants.NUM_0 : shelfUseCount;
            shelfValidCount = shelfCount - shelfUseCount;
        }
        return shelfValidCount;
    }

    public void setShelfValidCount(Integer shelfValidCount) {
        this.shelfValidCount = shelfValidCount;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Long getSystemTimeMillis() {
        return systemTimeMillis;
    }

    public void setSystemTimeMillis(Long systemTimeMillis) {
        this.systemTimeMillis = systemTimeMillis;
    }
}
package com.mobvista.okr.dto.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * order_info
 *
 * @author
 */
public class OrderInfoDTO implements Serializable {
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

    private List<Byte> statusList;

    private String statusText;

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
     * 创建人
     */
    private String createUserName;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 秘钥
     */
    private String passWord;

    /**
     * 是否是管理员权限
     */
    private Boolean adminRole = false;

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShelfTypeText() {
        return shelfTypeText;
    }

    public void setShelfTypeText(String shelfTypeText) {
        this.shelfTypeText = shelfTypeText;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public List<Byte> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<Byte> statusList) {
        this.statusList = statusList;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }


    public Boolean isAdminRole() {
        return adminRole;
    }

    public void setAdminRole(Boolean adminRole) {
        this.adminRole = adminRole;
    }
}
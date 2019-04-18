package com.mobvista.okr.domain;

import java.io.Serializable;

/**
 * product_group
 *
 * @author
 */
public class ProductGroup implements Serializable {
    private Integer id;

    /**
     * 组合商品id
     */
    private Long groupProductId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品组合数量
     */
    private Integer groupCount;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGroupProductId() {
        return groupProductId;
    }

    public void setGroupProductId(Long groupProductId) {
        this.groupProductId = groupProductId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }


}
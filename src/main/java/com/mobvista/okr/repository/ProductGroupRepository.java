package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ProductGroup;
import com.mobvista.okr.dto.product.ProductGroupDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ProductGroupRepository继承基类
 *
 * @author guwei
 */
@Mapper
public interface ProductGroupRepository extends BaseRepository<ProductGroup> {


    /**
     * 批量插入组合商品信息
     *
     * @param list
     * @param groupProductId
     * @return
     */
    int insertList(@Param("list") List<ProductGroup> list, @Param("groupProductId") Long groupProductId);


    /**
     * 判断是否是组合单品
     *
     * @param productId
     * @return
     */
    int countIsGroupProductChild(@Param("productId") Long productId);

    /**
     * 删除组合
     *
     * @param groupProductId
     * @return
     */
    int deleteByGroupProductId(@Param("groupProductId") Long groupProductId);

    List<ProductGroupDTO> queryProductListByGroupId(@Param("groupProductId") Long groupProductId);
}
package com.mobvista.okr.repository;

import com.mobvista.okr.domain.ProductShelf;
import com.mobvista.okr.dto.product.ProductShelfDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ProductPutawayRepository继承基类
 *
 * @author guwei
 */
@Mapper
public interface ProductShelfRepository extends BaseRepository<ProductShelf> {

    /**
     * 根据项目id和项目状态查询上架信息
     *
     * @param productId
     * @param status
     * @return
     */
    List<ProductShelf> queryListByProductIdAndStatus(@Param("productId") Long productId, @Param("productShelfIdList") List<Long> productShelfIdList, @Param("status") Byte status);


    /**
     * 根据dto统计查询
     *
     * @param dto
     * @return
     */
    int countListByDto(@Param("dto") ProductShelfDTO dto);

    /**
     * 分页查询产品信息
     *
     * @param dto
     * @param start
     * @param size
     * @return
     */
    List<ProductShelfDTO> queryListByDto(@Param("dto") ProductShelfDTO dto, @Param("start") int start, @Param("size") int size);


    /**
     * 根据上架id查询上架信息
     *
     * @param productShelfId
     * @return
     */
    ProductShelfDTO queryProductShelfInfoById(@Param("productShelfId") Long productShelfId);

    /**
     * 根据上架id集合查询上架信息
     *
     * @param list
     * @return
     */
    List<ProductShelfDTO> queryProductShelfInfoByIdList(@Param("list") List<Long> list);


    /**
     * 根据上架商品id集合 修改竞拍类上架商品状态
     *
     * @param productShelfIdList
     * @param status
     * @return
     */
    int updateAuctionProductShelfStatusEndByIdList(@Param("productShelfIdList") List<Long> productShelfIdList, @Param("status") Byte status);

    /**
     * 根据上架商品id集合 修改兑换类上架商品状态
     *
     * @param productShelfIdList
     * @param status
     * @return
     */
    int updateExchangeProductShelfStatusDownByIdList(@Param("productShelfIdList") List<Long> productShelfIdList, @Param("status") Byte status);

    /**
     * 更新商品使用库存
     *
     * @param shelfId
     * @param count
     * @return
     */
    int updateProductUseCount(@Param("shelfId") Long shelfId, @Param("count") int count);


    /**
     * 获取自动上架商品的id
     *
     * @return
     */
    List<Long> queryAutoUpProductShelfId();

    /**
     * 自动上架的商品
     *
     * @return
     */
    int autoUpProductShelf();
}
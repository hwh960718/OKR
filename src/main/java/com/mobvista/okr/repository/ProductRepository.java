package com.mobvista.okr.repository;

import com.mobvista.okr.domain.Product;
import com.mobvista.okr.dto.product.ProductDTO;
import com.mobvista.okr.repository.base.BaseRepository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ProductRepository继承基类
 *
 * @author guwei
 */
@Mapper
public interface ProductRepository extends BaseRepository<Product> {


    /**
     * 修改项目的状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateProductStatus(@Param("id") Long id, @Param("status") Byte status);

    /**
     * 根据dto统计查询
     *
     * @param dto
     * @return
     */
    int countListByDto(@Param("dto") ProductDTO dto);

    /**
     * 分页查询产品信息
     *
     * @param dto
     * @param start
     * @param size
     * @return
     */
    List<Product> queryListByDto(@Param("dto") ProductDTO dto, @Param("start") int start, @Param("size") int size);


    /**
     * 更新商品锁定库存
     *
     * @param productId
     * @param lockCount
     * @return
     */
    int updateProductLockInventory(@Param("productId") Long productId, @Param("lockCount") int lockCount);

    /**
     * 更新商品可用库存
     *
     * @param productId
     * @param count
     * @return
     */
    int updateProductValidAndUsedInventory(@Param("productId") Long productId, @Param("count") int count);

    /**
     * 更新商品锁定库存到已使用库存
     *
     * @param productId
     * @param count
     * @return
     */
    int updateProductUnLock2UsedInventory(@Param("productId") Long productId, @Param("count") int count);


}
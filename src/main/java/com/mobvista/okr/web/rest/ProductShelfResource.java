package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.product.ProductShelfDTO;
import com.mobvista.okr.enums.product.ProductShelfStatusEnum;
import com.mobvista.okr.service.product.ProductShelfService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/14 17:23
 */
@RestController
@RequestMapping("/api/productShelf")
public class ProductShelfResource {

    @Resource
    private ProductShelfService productShelfService;


    /**
     * 分页查询
     *
     * @param dto
     * @param pageable
     * @return
     * @version v3.1
     */
    @GetMapping("list")
    public CommonResult list(ProductShelfDTO dto, Pageable pageable) {
        return productShelfService.list(dto, pageable, false);
    }

    /**
     * 根据上架id查询上架信息
     *
     * @param productShelfId
     * @return
     */
    @GetMapping("queryProductShelfInfoById")
    public CommonResult queryProductShelfInfoById(Long productShelfId) {
        return productShelfService.queryProductShelfInfoById(productShelfId);
    }


    /**
     * 根据上架id查询上架信息
     *
     * @param productShelfId
     * @return
     */
    @GetMapping("queryEnableProductShelfInfoById")
    public CommonResult queryEnableProductShelfInfoById(Long productShelfId) {
        return productShelfService.queryEnableProductShelfInfoById(productShelfId);
    }


    /**
     * 新增上架信息
     *
     * @param dto
     * @return
     * @version V3.1
     */
    @PostMapping("saveShelfInfo")
    public CommonResult saveShelfInfo(@RequestBody ProductShelfDTO dto) {
        return productShelfService.saveShelfInfo(dto);
    }


    /**
     * 修改上架信息
     *
     * @param dto
     * @return
     * @version V3.1
     */
    @PostMapping("updateShelfInfo")
    public CommonResult updateShelfInfo(@RequestBody ProductShelfDTO dto) {
        return productShelfService.updateShelfInfo(dto);
    }


    /**
     * 查询兑换商品上架信息
     *
     * @param pageable
     * @return
     */
    @GetMapping("queryProductShelfInfo")
    public CommonResult queryProductShelfInfoExchange(ProductShelfDTO dto, Pageable pageable) {
        ProductShelfDTO queryDto = new ProductShelfDTO();
        if (null != dto && null != dto.getType()) {
            queryDto.setType(dto.getType());
        }
        queryDto.setStatus(ProductShelfStatusEnum.UP.getCode());
        return productShelfService.list(queryDto, pageable, true);
    }

}

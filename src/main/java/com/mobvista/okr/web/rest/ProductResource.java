package com.mobvista.okr.web.rest;

import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.AwsConstants;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.dto.product.ProductDTO;
import com.mobvista.okr.enums.product.ProductGroupEnum;
import com.mobvista.okr.enums.product.ProductStatusEnum;
import com.mobvista.okr.service.product.ProductService;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Weier Gu (顾炜)
 * @date 2018/9/7 14:40
 */
@RestController
@RequestMapping("/api/product")
public class ProductResource {

    @Resource
    private ProductService productService;


    /**
     * 分页查询
     *
     * @param dto
     * @param pageable
     * @return
     * @version v3.1
     */
    @GetMapping("list")
    public CommonResult list(ProductDTO dto, Pageable pageable) {
        return productService.list(dto, pageable);
    }


    /**
     * 根据商品id查询商品信息
     *
     * @param productId
     * @return
     * @version V3.1
     */
    @GetMapping("queryProductInfo")
    public CommonResult queryProductInfo(Long productId) {
        return productService.queryProductInfo(productId);
    }

    /**
     * 查询可用商品
     *
     * @return
     * @version v3.1
     */
    @GetMapping("queryEnableProductList")
    public CommonResult queryEnableProductList(String productName) {
        ProductDTO queryDto = new ProductDTO();
        queryDto.setName(productName);
        queryDto.setStatus(ProductStatusEnum.Enable.getCode());
        queryDto.setIsGroup(ProductGroupEnum.NO.getCode());
        queryDto.setValidCount(1);
        return productService.queryEnableProductListByNameAndGroup(queryDto);
    }


    /**
     * 查询所有可用商品
     * 上架管理中使用该接口
     *
     * @return
     * @version v3.1
     */
    @GetMapping("queryAllEnableProductList")
    public CommonResult queryAllEnableProductList(String productName) {
        ProductDTO queryDto = new ProductDTO();
        queryDto.setName(productName);
        queryDto.setStatus(ProductStatusEnum.Enable.getCode());
        queryDto.setValidCount(1);
        return productService.queryEnableProductListByNameAndGroup(queryDto);
    }

    /**
     * 新增商品信息
     *
     * @param dto
     * @return
     * @version v3.1
     */
    @PostMapping("saveProductInfo")
    public CommonResult saveProductInfo(@RequestBody ProductDTO dto) {
        try {
            return productService.saveProductInfo(dto);
        } catch (Exception e) {
            return CommonResult.error(e.getMessage());
        }
    }


    /**
     * 更新商品信息
     *
     * @param dto
     * @return
     * @version V3.1
     */
    @PostMapping("updateProductInfo")
    public CommonResult updateProductInfo(@RequestBody ProductDTO dto) {
        try {
            return productService.updateProductInfo(dto);
        } catch (RuntimeException e) {
            return CommonResult.error(e.getMessage());
        } catch (Exception e) {
            return CommonResult.error("更新商品信息异常");
        }
    }


    /**
     * 商品启用
     *
     * @param productId
     * @return
     * @version v3.1
     */
    @PostMapping("updateProductStatus/enable")
    public CommonResult updateProductStatusEnable(@RequestParam("productId") Long productId) {
        return productService.updateProductStatus(productId, ProductStatusEnum.Enable.getCode());
    }

    /**
     * 商品禁用
     *
     * @param productId
     * @return
     * @version v3.1
     */
    @PostMapping("updateProductStatus/disable")
    public CommonResult updateProductStatusDisable(@RequestParam("productId") Long productId) {
        return productService.updateProductStatus(productId, ProductStatusEnum.Disable.getCode());
    }

    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws IOException
     * @version v3.1
     */
    @PostMapping("uploadImage")
    public CommonResult uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        AWSResponseDTO awsResponseDTO = AWS3ClientUtils.uploadFileToBucket(AwsConstants.PICTURE_PATH_PRODUCT, file.getInputStream(), file.getOriginalFilename());
        return CommonResult.success(awsResponseDTO);
    }

}

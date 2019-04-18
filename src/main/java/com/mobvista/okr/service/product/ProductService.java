package com.mobvista.okr.service.product;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.domain.Product;
import com.mobvista.okr.domain.ProductGroup;
import com.mobvista.okr.domain.ProductShelf;
import com.mobvista.okr.dto.AWSResponseDTO;
import com.mobvista.okr.dto.product.ProductDTO;
import com.mobvista.okr.dto.product.ProductGroupDTO;
import com.mobvista.okr.enums.product.ProductGroupEnum;
import com.mobvista.okr.enums.product.ProductShelfStatusEnum;
import com.mobvista.okr.enums.product.ProductStatusEnum;
import com.mobvista.okr.repository.ProductGroupRepository;
import com.mobvista.okr.repository.ProductRepository;
import com.mobvista.okr.repository.ProductShelfRepository;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.AwsPictureProcessUtil;
import com.mobvista.okr.util.aws.AWS3ClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品服务
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/7 13:49
 */
@Service
public class ProductService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Resource
    private ProductRepository productRepository;
    @Resource
    private ProductGroupRepository productGroupRepository;
    @Resource
    private ProductShelfRepository productShelfRepository;


    /**
     * 分页查询商品信息
     *
     * @param dto
     * @param pageable
     * @return
     */
    public CommonResult list(ProductDTO dto, Pageable pageable) {
        int count = productRepository.countListByDto(dto);
        List<Product> list = Lists.newArrayList();
        if (count > 0) {
            list = productRepository.queryListByDto(dto, pageable.getPageNumber() * pageable.getPageSize(), pageable.getPageSize());
        }
        return CommonResult.success(new PageImpl<>(list, pageable, count));
    }


    /**
     * 查询商品信息
     *
     * @param productId
     * @return
     */
    public CommonResult queryProductInfo(Long productId) {
        if (null == productId || productId <= 0) {
            return CommonResult.error("入参不能为空");
        }
        Product product = productRepository.selectByPrimaryKey(productId);
        if (product == null) {
            return CommonResult.error("商品信息不存在");
        }
        ProductDTO productDTO = convertPo2Dto(product);
        if (ProductGroupEnum.checkIsGroup(product.getIsGroup())) {
            List<ProductGroupDTO> groupDTOList = productGroupRepository.queryProductListByGroupId(productId);
            productDTO.setProductGroupDTOList(groupDTOList);
        }
        String picturePath = product.getPicturePath();
        if (StringUtils.isNotBlank(picturePath)) {
            List<AWSResponseDTO> pathList = AWS3ClientUtils.getTempLinkUrlDtoListByKeys(Lists.newArrayList(picturePath.split(CommonConstants.APPEND_COMMA)));
            productDTO.setPicturePathList(pathList);
        }
        return CommonResult.success(productDTO);
    }


    /**
     * 查询可用商品
     *
     * @return
     */
    public CommonResult queryEnableProductListByNameAndGroup(ProductDTO queryDto) {
        
        List<Product> list = productRepository.queryListByDto(queryDto, CommonConstants.NUM_0, CommonConstants.NUM_0);
        List<ProductDTO> productDTOList = Lists.newArrayList();
        if (list != null && list.size() > 0) {
            for (Product po : list) {
                ProductDTO dto = convertPo2Dto(po);
                productDTOList.add(dto);
            }
        }
        return CommonResult.success(productDTOList);
    }

    /**
     * 新建商品信息
     *
     * @param productDTO
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public CommonResult saveProductInfo(ProductDTO productDTO) {
        try {
            if (null == productDTO) {
                return CommonResult.error("入参不能为空");
            }
            if (StringUtils.isBlank(productDTO.getName())) {
                return CommonResult.error("商品名称不能为空");
            }
            if (StringUtils.isBlank(productDTO.getPicturePath())) {
                return CommonResult.error("至少上传一幅图片信息");
            }
            List<ProductGroup> productGroupList = Lists.newArrayList();
            Map<Long, Integer> productCountMap = Maps.newHashMap();
            //判断是否是组合商品
            if (ProductGroupEnum.checkIsGroup(productDTO.getIsGroup())) {
                List<ProductGroupDTO> list = productDTO.getProductGroupDTOList();
                if (null == list || list.size() == 0) {
                    return CommonResult.error("组合商品子商品不能为空");
                }
                Integer validCount = productDTO.getValidCount();
                convertProductGroupDto2Po(list, productGroupList, productCountMap, validCount);
                //判断组合商品库存是充足
                for (Map.Entry<Long, Integer> entry : productCountMap.entrySet()) {
                    int i = productRepository.updateProductLockInventory(entry.getKey(), entry.getValue());
                    if (i == 0) {
                        throw new RuntimeException("库存不足");
                    }
                }
            }
            Product product = convertDto2Po(productDTO, false);
            product.setInventoryCount(product.getValidCount());
            product.setStatus(ProductStatusEnum.Enable.getCode());
            product.setUsedCount(0);
            product.setLockedCount(0);
            productRepository.insert(product);
            //保存组合商品id
            if (productGroupList.size() > 0) {
                productGroupRepository.insertList(productGroupList, product.getId());
            }
            return CommonResult.success();
        } catch (RuntimeException e) {
            log.error("新增商品信息----异常", e);
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error("新增商品信息----异常", e);
            throw new RuntimeException("新增失败");
        }
    }

    private void convertProductGroupDto2Po(List<ProductGroupDTO> dtoList, List<ProductGroup> productGroupList, Map<Long, Integer> productCountMap, Integer validCount) {
        for (ProductGroupDTO dto : dtoList) {
            productGroupList.add(convertProductGroupDto2Po(dto));
            productCountMap.put(dto.getProductId(), dto.getGroupCount() * validCount);
        }
    }

    private ProductGroup convertProductGroupDto2Po(ProductGroupDTO dto) {
        ProductGroup po;
        po = new ProductGroup();
        po.setProductId(dto.getProductId());
        po.setGroupCount(dto.getGroupCount());
        return po;
    }


    /**
     * 更新商品信息
     *
     * @param productDTO
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult updateProductInfo(ProductDTO productDTO) {
        if (null == productDTO) {
            return CommonResult.error("入参不能为空");
        }
        if (StringUtils.isBlank(productDTO.getPicturePath())) {
            return CommonResult.error("至少上传一幅图片信息");
        }
        Long productId = productDTO.getId();
        if (null == productId) {
            return CommonResult.error("商品ID不存在");
        }
        Product product = productRepository.selectByPrimaryKey(productId);
        if (null == product) {
            return CommonResult.error("商品信息不存在");
        }
        List<ProductGroup> productGroupList = Lists.newArrayList();
        Map<Long, Integer> productCountMap = Maps.newHashMap();
        //将当期非组合商品修改为组合商品，判断该商品是否是组合单品
        List<ProductGroupDTO> productGroupDTOList = productDTO.getProductGroupDTOList();
        if (!ProductGroupEnum.checkIsGroup(product.getIsGroup()) && ProductGroupEnum.checkIsGroup(productDTO.getIsGroup())) {
            if (null == productGroupDTOList || productGroupDTOList.size() == 0) {
                return CommonResult.error("组合商品子商品不能为空");
            }
            int count = productGroupRepository.countIsGroupProductChild(productId);
            if (count > 0) {
                return CommonResult.error("不能修改为组合商品，该商品为组合单品");
            }
            Integer validCount = productDTO.getValidCount() - product.getValidCount();
            convertProductGroupDto2Po(productDTO.getProductGroupDTOList(), productGroupList, productCountMap, validCount);
            //判断组合商品库存是充足
            for (Map.Entry<Long, Integer> entry : productCountMap.entrySet()) {
                int i = productRepository.updateProductLockInventory(entry.getKey(), entry.getValue());
                if (i == 0) {
                    throw new RuntimeException("库存不足");
                }
            }
        }
        Product updatePo = convertDto2Po(productDTO, true);
        updatePo.setId(productId);
        updatePo.setInventoryCount(product.getInventoryCount() + productDTO.getValidCount() - product.getValidCount());
        productRepository.updateByPrimaryKeySelective(updatePo);
        //删除组合单品
        productGroupRepository.deleteByGroupProductId(productId);
        if (productGroupList.size() > 0) {
            productGroupRepository.insertList(productGroupList, productId);
        }
        AwsPictureProcessUtil.deletePicture(product.getPicturePath(), productDTO.getPicturePath());
        return CommonResult.success();
    }

    /**
     * 修改商品状态
     *
     * @param productId
     * @param status
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public CommonResult updateProductStatus(Long productId, Byte status) {
        Product product = productRepository.selectByPrimaryKey(productId);
        if (null == product) {
            return CommonResult.error("商品信息不存在");
        }
        //判断是否是禁用
        if (ProductStatusEnum.Disable.equals(status) || ProductStatusEnum.Delete.equals(status)) {
            //判断项目是否上架
            List<ProductShelf> productShelfList = productShelfRepository.queryListByProductIdAndStatus(productId, null, ProductShelfStatusEnum.UP.getCode());
            if (null != productShelfList && productShelfList.size() > 0) {
                return CommonResult.error("该商品上架中无法下架");
            }
        }
        //更新商品状态
        productRepository.updateProductStatus(productId, status);
        return CommonResult.success();
    }

    /**
     * dto 转 po
     *
     * @param dto
     * @return
     */
    private Product convertDto2Po(ProductDTO dto, boolean isModify) {
        Product po = new Product();
        BeanUtils.copyProperties(dto, po);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        Date date = new Date();
        if (!isModify) {
            po.setCreateUserId(currentUserId);
            po.setCreateDate(date);
        }
        po.setModifyDate(date);
        po.setModifyUserId(currentUserId);
        return po;
    }

    /**
     * po 转 dto
     *
     * @param po
     * @return
     */
    private ProductDTO convertPo2Dto(Product po) {
        ProductDTO dto = new ProductDTO();
        BeanUtils.copyProperties(po, dto);
        return dto;
    }
}

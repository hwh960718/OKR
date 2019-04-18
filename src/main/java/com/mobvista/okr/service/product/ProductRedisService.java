package com.mobvista.okr.service.product;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mobvista.okr.constants.CommonConstants;
import com.mobvista.okr.constants.RedisConstants;
import com.mobvista.okr.domain.ProductShelf;
import com.mobvista.okr.service.RedisService;
import com.mobvista.okr.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/15 11:11
 */
@Service
public class ProductRedisService {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(ProductRedisService.class);


    @Resource
    public RedisService redisService;


    /**
     * 获取订单号
     *
     * @return
     */
    public String getOrderNo() {
        String date = DateUtil.toStringYyyyMMdd(new Date());
        String key = RedisConstants.ORDER_NO_KEY + date;
        Long increment = redisService.increment(key);
        //设置redis 24小时过期
        redisService.expireByHours(key, 24L);
        return date + formatLong2String(increment);
    }


    /**
     * 获取当前上架商品金额
     *
     * @param shelfId
     * @return
     */
    public BigDecimal getCurrentProductShelfOrderPrice(Long shelfId) {
        BigDecimal currentPrice = BigDecimal.ZERO;
        String value = redisService.get(RedisConstants.PRODUCT_SHELF_ORDER_CURRENT_PRICE_KEY + shelfId);
        if (StringUtils.isNotBlank(value)) {
            currentPrice = BigDecimal.valueOf(Double.valueOf(value));
        }
        return currentPrice;
    }

    /**
     * 设置当前商品价格
     *
     * @param shelfId
     * @param price
     */
    public void setCurrentProductShelfOrderPrice(Long shelfId, BigDecimal price) {
        redisService.set(RedisConstants.PRODUCT_SHELF_ORDER_CURRENT_PRICE_KEY + shelfId, String.valueOf(price), RedisConstants.EXPIRE_TIME_1H);
    }


    /**
     * 将数字格式化成字符串
     *
     * @param num
     * @return
     */
    private String formatLong2String(Long num) {
        DecimalFormat decimalFormat = new DecimalFormat("000000");
        return decimalFormat.format(num);
    }


    /**
     * 保存上架商品信息
     *
     * @param productShelf
     */
    public void saveProductShelf(ProductShelf productShelf) {
        try {
            redisService.set(RedisConstants.PRODUCT_SHELF_KEY + productShelf.getId(), JSON.toJSONString(productShelf));
        } catch (Exception e) {
            log.error("保存上架商品信息 ---- 移除", e);
        }
    }

    /**
     * 移除上架商品信息
     *
     * @param shelfId
     */
    public void removeProductShelf(Long shelfId) {
        try {
            redisService.remove(RedisConstants.PRODUCT_SHELF_KEY + shelfId);
        } catch (Exception e) {
            log.error("移除上架商品信息 ---- 异常", e);
        }
    }

    /**
     * 获取上架资产信息
     *
     * @param shelfId
     * @return
     */
    public ProductShelf getProductShelf(Long shelfId) {
        String value = redisService.get(RedisConstants.PRODUCT_SHELF_KEY + shelfId);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, ProductShelf.class);
        }
        return null;
    }

    /**
     * 根据key获取上架商品信息
     *
     * @param key
     * @return
     */
    public ProductShelf getProductShelfByKey(String key) {
        String value = redisService.get(key);
        if (StringUtils.isNotBlank(value)) {
            return JSON.parseObject(value, ProductShelf.class);
        }
        return null;
    }

    /**
     * 获取所有的上架商品信息
     *
     * @return
     */
    public List<ProductShelf> getAllProductShelf() {
        List<ProductShelf> productShelfList = Lists.newArrayList();
        List<String> keyList = redisService.getLikeKey(RedisConstants.PRODUCT_SHELF_KEY + CommonConstants.ASTERISK);
        if (null != keyList && keyList.size() > 0) {
            for (String key : keyList) {
                productShelfList.add(getProductShelfByKey(key));
            }
        }
        return productShelfList;
    }


    /**
     * 订单用户锁
     *
     * @param shelfId
     * @param userId
     * @return
     */
    public boolean orderLock(Long shelfId, Long userId, int expireTime) {
        return redisService.lock(RedisConstants.REDIS_LOCK_KEY_PRODUCT_ORDER + shelfId + CommonConstants.APPEND_COLON + userId, expireTime);

    }

    public void orderUnLock(Long shelfId, Long userId) {
        redisService.unLock(RedisConstants.REDIS_LOCK_KEY_PRODUCT_ORDER + shelfId + CommonConstants.APPEND_COLON + userId);

    }
}

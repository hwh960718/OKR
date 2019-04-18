package com.mobvista.okr.service;

import com.google.common.collect.Lists;
import com.mobvista.okr.constants.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 注释：redis工具类
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-26 上午10:41
 */
@Component
public class RedisService {
    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(RedisService.class);


    @Resource
    private RedisTemplate stringRedisTemplate;

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public String get(final String key) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 批量删除对应的key
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys.size() > 0) {
            stringRedisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    public void remove(final String key) {
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断缓存中是否有对应的key
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, String value) {
        boolean result = false;
        try {
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @param expireTime 过期时间
     * @return
     */
    public boolean set(final String key, String value, Long expireTime) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value);
        stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 设置缓存时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    public boolean expire(final String key, Long expireTime) {
        stringRedisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 设置缓存时间
     *
     * @param key
     * @param expireTime
     * @return
     */
    public boolean expireByHours(final String key, Long expireTime) {
        stringRedisTemplate.expire(key, expireTime, TimeUnit.HOURS);
        return true;
    }

    /**
     * 从hash中根据指定key获取数据
     *
     * @param key
     * @param hkey hash key
     * @return
     */
    public String hmget(String key, String hkey) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        return operations.get(key, hkey);
    }

    /**
     * 在hash中设置数据
     *
     * @param key
     * @param hkey  hash key
     * @param value
     * @return
     */
    public void hmset(String key, String hkey, String value) {
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        operations.put(key, hkey, value);
    }

    /**
     * 根据key递增
     *
     * @param key
     * @return
     */
    public Long increment(String key) {
        return stringRedisTemplate.opsForValue().increment(key, 1L);
    }

    /**
     * 根据key获取map
     *
     * @param key
     * @return
     */
    public Map<String, Object> hGetAll(String key) {
        return stringRedisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置map数据
     *
     * @param key
     * @param map
     */
    public void hSet(String key, Map<String, Object> map) {
        stringRedisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 锁定，只锁定一次
     *
     * @param key
     * @param expireTime
     * @return
     */
    public boolean lock(String key, int expireTime) {
        // 获取锁的超时时间，超过这个时间则放弃获取锁
        return (boolean) stringRedisTemplate.execute((RedisCallback) connection -> {
            try {
                byte[] keyBytes = key.getBytes();
                if (connection.setNX(keyBytes, RedisConstants.LOCK.getBytes())) {
                    connection.expire(keyBytes, expireTime);
                    return true;
                }
            } catch (final Exception e) {
                log.error("lock key ({}) error", key, e);
            } finally {
                connection.close();
            }
            return false;
        });

    }


    /**
     * 给redis解锁
     *
     * @param key
     */
    public void unLock(String key) {
        stringRedisTemplate.execute((RedisCallback) connection -> {
            try {
                if (connection.exists(key.getBytes())) {
                    connection.del(key.getBytes());
                }
            } catch (Exception e) {
                log.error("unlock key ({}) error", key, e);
            } finally {
                connection.close();
            }
            return null;
        });
    }


    /**
     * 根据key模糊查询所有的key
     *
     * @param key
     * @return
     */
    public List<String> getLikeKey(String key) {
        Set<String> keys = stringRedisTemplate.keys(key);
        return Lists.newArrayList(keys);
    }
}

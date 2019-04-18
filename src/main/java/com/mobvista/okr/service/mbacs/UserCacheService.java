package com.mobvista.okr.service.mbacs;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.constants.UserLoginConstants;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.mbacs.UserInfo;
import com.mobvista.okr.dto.mbacs.UserPerm;
import com.mobvista.okr.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户缓存
 *
 * @author 顾炜[GuWei]
 */
@Service
public class UserCacheService {

    private static final Logger LOGGER = Logger.getLogger(UserCacheService.class);

    @Resource
    private RedisService redisService;


    /**
     * 设置用户数据
     *
     * @param userInfo
     * @param expires
     * @return
     */

    public void setUserInfo(UserInfo userInfo, Long expires) {
        try {
            Long id = userInfo.getProfile().getId();
            String value = JSON.toJSONString(userInfo);
            redisService.set(UserLoginConstants.USER_KEY + ":" + id, value, expires);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("设置用户信息缓存异常", e);
        }
    }

    /**
     * 删除用户数据
     *
     * @param userId
     * @return
     */

    public void delUserInfo(Long userId) {
        try {
            redisService.remove(UserLoginConstants.USER_KEY + ":" + userId);
        } catch (Exception e) {
            LOGGER.error("删除用户数据----异常", e);
        }
    }


    /**
     * 获取登录用户缓存数据（登录时，缓存）
     *
     * @param userId
     * @return
     */

    public UserInfo getUserInfo(Long userId) {
        try {
            String value = redisService.get(UserLoginConstants.USER_KEY + ":" + userId);
            if (value != null) {
                return JSON.parseObject(value, UserInfo.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取登录用户缓存数据异常", e);
        }
        return null;
    }


    /**
     * 用户登录时间缓存key
     *
     * @param userId
     * @param date
     */
    public void setUserLoginTime(Long userId, Long date) {
        try {
            redisService.set(UserLoginConstants.USER_LOGIN_HKEY + ":" + String.valueOf(userId), String.valueOf(date), UserLoginConstants.USER_EXPIRE);
        } catch (Exception e) {
            LOGGER.error("用户登录时间缓存key----异常", e);
        }
    }

    /**
     * 获取用户登录信息
     *
     * @param userId
     * @return
     */
    public String getUserLoginTime(Long userId) {
        try {
            return redisService.get(UserLoginConstants.USER_LOGIN_HKEY + ":" + String.valueOf(userId));
        } catch (Exception e) {
            LOGGER.error("获取用户登录信息---异常", e);
        }
        return null;
    }

    /**
     * 删除用户登录信息
     *
     * @param userId
     */
    public void delUserLoginTime(Long userId) {
        try {
            redisService.remove(UserLoginConstants.USER_LOGIN_HKEY + ":" + String.valueOf(userId));
        } catch (Exception e) {
            LOGGER.error("删除用户登录信息----异常", e);
        }
    }

    /**
     * 获取缓存用户
     *
     * @param id
     * @return
     */

    public UserProfile getCacheUser(Long id) throws Exception {
        UserProfile userProfile = null;
        String json = redisService.get(UserLoginConstants.USER_CACHE_KEY_PREFIX + ":" + id);
        if (StringUtils.isNotBlank(json)) {
            userProfile = JSON.parseObject(json, UserProfile.class);
        }
        return userProfile;
    }

    /**
     * 缓存用户
     *
     * @param profile
     */

    public void setCacheUser(UserProfile profile) {
        try {
            redisService.set(UserLoginConstants.USER_CACHE_KEY_PREFIX + ":" + profile.getId(), JSON.toJSONString(profile), UserLoginConstants.USER_EXPIRE);
        } catch (Exception e) {
            LOGGER.error("缓存用户----异常", e);
        }
    }

    /**
     * 用户权限信息缓存
     *
     * @param userId
     * @param userPerm
     */
    public void setCacheUserPerm(Long userId, UserPerm userPerm) {
        String userPermJson = JSON.toJSONString(userPerm);
        try {
            redisService.set(UserLoginConstants.USER_PERM_KEY + ":" + String.valueOf(userId), userPermJson, UserLoginConstants.USER_EXPIRE);
        } catch (Exception e) {
            LOGGER.error("用户权限信息缓存----异常", e);
        }
    }

    /**
     * 更新用户缓存时间
     *
     * @param userId
     */
    public void setCacheUserPermExpire(Long userId) {
        try {
            redisService.expire(UserLoginConstants.USER_PERM_KEY + ":" + String.valueOf(userId), UserLoginConstants.USER_EXPIRE);
        } catch (Exception e) {
            LOGGER.error("更新用户缓存时间----异常", e);
        }
    }


    /**
     * 获取用户权限缓存信息
     *
     * @param userId
     * @return
     */
    public UserPerm getCacheUserPerm(Long userId) {
        UserPerm userPerm = null;
        try {
            String value = redisService.get(UserLoginConstants.USER_PERM_KEY + ":" + String.valueOf(userId));
            if (StringUtils.isNotBlank(value)) {
                userPerm = JSON.parseObject(value, UserPerm.class);
            }
        } catch (Exception e) {
            LOGGER.error("获取用户权限缓存信息----异常", e);
        }
        return userPerm;
    }


    /**
     * 删除用户权限缓存
     *
     * @param userId
     */
    public void delCacheUserPerm(Long userId) {
        try {
            redisService.remove(UserLoginConstants.USER_PERM_KEY + ":" + String.valueOf(userId));
        } catch (Exception e) {
            LOGGER.error("删除用户权限缓存----异常", e);
        }
    }
}

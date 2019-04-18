package com.mobvista.okr.constants;

/**
 * 注释：用户登录相关常量
 *
 * @author 顾炜[GuWei]
 */
public final class UserLoginConstants {
    /**
     * okr用户key
     */
    public final static String USER_KEY = "okr:user:info";

    /**
     * okr用户权限key
     */
    public final static String USER_PERM_KEY = "okr:user:perm";

    /**
     * 用户session过期时间，3小时 = 10800秒
     */
    public final static Long USER_EXPIRE = 10800L;

    /**
     * 用户登录时间缓存key
     */
    public final static String USER_LOGIN_HKEY = "okr:user:logintime";

    /**
     * 用户cookie name
     */
    public final static String USER_COOKIE_NAME = "auth";

    /**
     * 用户cookie path
     */
    public final static String USER_COOKIE_PATH = "/";

    /**
     * 用户cookie domain
     */
    public final static String USER_COOKIE_DOMAIN = "okr.mobvista.com";

    /**
     * 用户key
     */
    public final static String USER_CACHE_KEY_PREFIX = "okr:user:";


}

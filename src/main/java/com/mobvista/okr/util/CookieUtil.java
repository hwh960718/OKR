package com.mobvista.okr.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 注释：cookie工具类
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-22 下午4:00
 */
public class CookieUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);

    //auth cookie name
    public static final String AUTH_NAME = "auth";

    /* 多语言cookies*/
    public static final String LNG_COOKIES = "lng";

    /**
     * 获取指定cookie的值
     *
     * @param request
     * @param name
     * @return String
     */
    public static String getCookieByName(HttpServletRequest request, String name) {
        if (name == null || name.length() == 0) {
            return null;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }

        String value = null;
        try {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (name.equals(cookie.getName())) {
                    value = cookie.getValue();
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("根据name获取cookie值异常，name=" + name, e);
        }
        return value;
    }
}

package com.mobvista.okr.util;

/**
 * 注释：响应状态
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-22 下午6:47
 *
 * @author Weier Gu (顾炜)
 */
public class HTTPStatus {
    /**
     * 成功
     */
    public static final int OK = 0;
    /**
     * 错误
     */
    public static final int ERR = 1;
    /**
     * 警告
     */
    public static final int WARN = 2;
    /**
     * 重新登录
     */
    public static final int RELOGIN = 401;
    /**
     * 权限不足
     */
    public static final int NO_AUTHORITY = 403;
}

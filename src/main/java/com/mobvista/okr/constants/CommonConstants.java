package com.mobvista.okr.constants;

/**
 * 通用常量
 *
 * @author Weier Gu (顾炜)
 */
public final class CommonConstants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String UPLOAD_MODULE_USERFACE = "userface/";

    //反馈临时文件夹
    public static final String FEED_BACK_TEMP = "/temp/feedback/";

    /**
     * 地址 okr系统说明文档URL
     */
    public static final String URL_OKR_SYSTEM_DOC = "http://okr.mobvista.com/doc/";

    /**
     * 系统标识
     */
    public static final String SYSTEM = "okr";

    /**
     * 连接符 逗号
     */
    public final static String APPEND_COMMA = ",";
    /**
     * 连接符 冒号
     */
    public final static String APPEND_COLON = ":";
    /**
     * 连接符 分号
     */
    public final static String APPEND_1_EN = ";";
    /**
     * 连接符 斜杠
     */
    public final static String APPEND_SLASH = "/";

    /**
     * 连接符 下划线
     */
    public final static String APPEND_UNDER_LINE = "_";
    /**
     * 连接符 中划线
     */
    public final static String APPEND_MID_LINE = " - ";

    /**
     * 链接字符串
     */
    public final static String APPEND_2_OKR = "_okr_";
    /**
     * 星号
     */
    public final static String ASTERISK = "*";

    public final static String APPEND_ZHI = " 至 ";


    public final static int NUM_100 = 100;

    public final static int NUM_0 = 0;

    public final static int NUM_5 = 5;
    public final static int NUM_1 = 1;
    /**
     * 负数
     */
    public final static int NUM_1_NEGATIVE = -1;


    private CommonConstants() {
    }
}

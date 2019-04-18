package com.mobvista.okr.util;

import java.util.regex.Pattern;

/**
 * Created by jiahuijie on 2017/5/2.
 */
public class CommonUtil {

    public static final String REGX_MOBILE = "^[0-9]{11}$";
    public static final String REGX_EMAIL = "^[A-Za-z0-9\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * 校验手机号
     * @param str
     * @return
     */
    public static boolean matchMobile(String str) {
        return Pattern.matches(REGX_MOBILE, str);
    }

    /**
     * 校验邮箱
     * @param str
     * @return
     */
    public static boolean matchEmail(String str) {
        return Pattern.matches(REGX_EMAIL, str);
    }
}

package com.mobvista.okr.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.Map;

/**
 * 国际化用工具类
 *
 * @author yp
 * @date 2017/7/10
 */
@Component
public class LocaleUtil {

    private static final Logger log = LoggerFactory.getLogger(LocaleUtil.class);

    public final static String ALL = "ALL";
    public final static String STR_ENUM = "ENUM_";
    public final static String EN = "en";
    public final static String ZH = "zh";
    public final static String INTERVAL = "#";


    @Autowired
    protected MessageSource messageSource;

    private static MessageSource ms;

    @PostConstruct
    public void initialize() {
        //@Component will call construct
        ms = this.messageSource;
    }


    /**
     * 获取用户locale
     *
     * @param request
     * @return
     */
    public static Locale getLocaleFromCookie(HttpServletRequest request) {

        String localeStr = CookieUtil.getCookieByName(request, CookieUtil.LNG_COOKIES);

        Locale locale = null;

        // 如果cookies中为空，则取当前用户浏览器的语言
        if (localeStr == null || "".equals(localeStr)) {
            locale = request.getLocale();
        } else if (EN.equals(localeStr)) {
            locale = Locale.ENGLISH;
        } else {
            locale = Locale.CHINESE;
        }
        return locale;
    }


    /**
     * 获取当前请求语言环境，默认中文
     *
     * @return zh en
     */
    public static String getLocaleString() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String localeStr = CookieUtil.getCookieByName(request, CookieUtil.LNG_COOKIES);

        if (StringUtils.isBlank(localeStr)) {
            localeStr = ZH;
        }
        return localeStr;
    }

    /**
     * 从资源文件中获取多语
     *
     * @param code   编码（资源文件）
     * @param locale 用户Locale
     * @return 多语
     */
    public static String getLang(String code, Locale locale) {
        String msg = null;
        try {
            msg = ms.getMessage(code, null, locale);
        } catch (Exception e) {
            log.error("未获取到【" + code + "】对应的多语言配置");
        }
        return msg;
    }

    public static String getLangForMoreFields(String code, Object... objects) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Locale locale = getLocaleFromCookie(request);
        String msg = ms.getMessage(code, null, locale);
        FormattingTuple ft = MessageFormatter.arrayFormat(msg, objects);
        return ft.getMessage();
    }

    /**
     * 从资源文件中获取多语
     *
     * @param code 编码（资源文件）
     * @return 多语
     */
    public static String getLang(String code) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Locale locale = getLocaleFromCookie(request);
        return getLang(code, locale);
    }


    /**
     * 通过map获取本地化描述
     * 曲线改良，一次拿到map，多次使用。
     *
     * @param enumValue
     * @param enumMap
     * @return
     */
    public static String getEnumLabelByMap(Object enumValue, Map<String, String> enumMap) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String localeStr = CookieUtil.getCookieByName(request, CookieUtil.LNG_COOKIES);

        String enumLabels = enumMap.get(String.valueOf(enumValue));
        String rst = "";
        rst = getString(localeStr, enumLabels, rst);
        return rst;
    }

    private static String getString(String localeStr, String enumLabels, String rst) {
        if (StringUtils.isNotBlank(enumLabels)) {
            String[] enumLabelArray = enumLabels.split(INTERVAL);
            if (enumLabelArray.length > 0) {
                if (EN.equals(localeStr)) {
                    rst = enumLabelArray.length > 1 ? enumLabelArray[1] : enumLabelArray[0];
                } else if (ZH.equals(localeStr)) {
                    rst = enumLabelArray[0];
                } else {
                    rst = enumLabelArray[0];
                }
            }
        }
        return rst;
    }

    /**
     * 判断当前请求是否为中文环境
     *
     * @return boolean ：中文：true 反之false
     */
    public static Boolean isZhForRequestLanguage() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (ZH.equals(CookieUtil.getCookieByName(request, CookieUtil.LNG_COOKIES))) {
            return true;
        }
        return false;
    }

}

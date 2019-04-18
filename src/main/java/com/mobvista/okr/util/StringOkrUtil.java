package com.mobvista.okr.util;

import com.google.common.collect.Lists;
import com.mobvista.okr.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 字符串工具函数
 *
 * @author Weier Gu (顾炜)
 */
public class StringOkrUtil {


    /**
     * 将字符串转为List<Long>
     *
     * @param str
     * @return
     */
    public static List<Long> convertStr2LongList(String str) {
        List<Long> list = Lists.newArrayList();
        if (StringUtils.isNotBlank(str)) {
            String[] split = str.split(CommonConstants.APPEND_COMMA);
            for (String s : split) {
                list.add(Long.valueOf(s));
            }
        }
        return list;
    }


    /**
     * 字符串拼接
     *
     * @param stringBuffer
     * @param value
     * @return
     */
    public static StringBuffer appendStringBuffer(StringBuffer stringBuffer, String value) {
        if (StringUtils.isNotBlank(stringBuffer)) {
            stringBuffer.append(CommonConstants.APPEND_COMMA);
        }
        stringBuffer.append(value);
        return stringBuffer;
    }
}

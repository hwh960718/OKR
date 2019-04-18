package com.mobvista.okr.util;

import com.google.common.collect.Lists;
import com.mobvista.okr.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * list工具类
 *
 * @author 顾炜(GUWEI)
 * @date 2018/5/22 10:55
 */
public class OKRListUtil {


    /**
     * 将Long list对象转sql字符串
     *
     * @param idList
     * @return
     */
    public static String list2SqlString(List idList) {
        return list2String(idList, "(", ")");
    }

    /**
     * 将Long list对象转sql字符串
     *
     * @param idList
     * @return
     */
    public static String list2String(List idList) {
        return list2String(idList, "", "");
    }


    /**
     * 将list对象转字符串
     *
     * @param idList
     * @return
     */
    public static String list2String(List idList, String flagBefore, String flagAfter) {
        if (null == idList || idList.size() == 0) {
            return null;
        }
        return idList.toString().replace("[", flagBefore).replace("]", flagAfter);
    }


    public static <T> List<T> string2List(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        try {
            String[] strArr = str.split(CommonConstants.APPEND_COMMA);
            List<T> list = Lists.newArrayList();
            for (String s : strArr) {
                list.add((T) s);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取list，若入参为null，返回一个新的ArrayList
     *
     * @param list
     * @return
     */
    public static <T> List<T> newArrayListIfNull(List<T> list) {
        return null == list ? Lists.newArrayList() : list;
    }
}

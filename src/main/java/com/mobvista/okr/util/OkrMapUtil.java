package com.mobvista.okr.util;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * OKR MAP 工具类处理
 *
 * @author Weier Gu (顾炜)
 * @date 2018/6/27 11:38
 */
public class OkrMapUtil {


    /**
     * 获取map，若入参为null，返回一个新的newHashMap
     *
     * @param map
     * @return
     */
    public static <K, V> Map<K, V> newHashMapIfNull(Map<K, V> map) {
        return null == map ? Maps.newHashMap() : map;
    }
}

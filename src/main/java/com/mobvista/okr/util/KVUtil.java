package com.mobvista.okr.util;

/**
 * 注释：用户kv对象
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-09 上午11:27
 */
public class KVUtil {
    private Long id;
    private String name;

    public KVUtil() {
    }

    public KVUtil(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

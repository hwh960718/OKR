package com.mobvista.okr.dto.mbacs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * 注释：菜单vo
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-22 下午6:07
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MenuVo implements Serializable {

    private static final long serialVersionUID = -3740648692416941465L;

    /**
     * 菜单url
     */
    private String path;
    /**
     * 菜单名称
     */
    private String title;
    /**
     * 标签
     */
    private String name;
    /**
     * 授权url
     */
    private String operUrl;
    /**
     * 菜单icon
     */
    private String icon;
    /**
     * 子菜单
     */
    private List<MenuVo> children;

    @JsonIgnore
    private String code;
    @JsonIgnore
    private String parentCode;
    /**
     * 目录级别
     */
    @JsonIgnore
    private int level;

    /**
     * 跳转路径
     */
    private String href;

    public MenuVo() {
    }

    public MenuVo(String path, String title, String name, String operUrl, String icon, List<MenuVo> children) {
        this.path = path;
        this.title = title;
        this.name = name;
        this.operUrl = operUrl;
        this.icon = icon;
        this.children = children;
    }

    public MenuVo(String path, String operUrl, String title, String name, String icon, String code, String parentCode, int level) {
        this.path = path;
        this.operUrl = operUrl;
        this.title = title;
        this.name = name;
        this.icon = icon;
        this.code = code;
        this.parentCode = parentCode;
        this.level = level;
    }

    public MenuVo(String path, String operUrl, String title, String name, String icon, String code, String parentCode, int level, String href) {
        this.path = path;
        this.title = title;
        this.name = name;
        this.operUrl = operUrl;
        this.icon = icon;
        this.code = code;
        this.parentCode = parentCode;
        this.level = level;
        this.href = href;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperUrl() {
        return operUrl;
    }

    public void setOperUrl(String operUrl) {
        this.operUrl = operUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

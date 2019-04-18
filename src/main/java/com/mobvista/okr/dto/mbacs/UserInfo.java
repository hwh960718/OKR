package com.mobvista.okr.dto.mbacs;

import com.mobvista.okr.domain.UserProfile;

import java.io.Serializable;
import java.util.List;

/**
 * 注释：用户信息
 * @author Weier Gu (顾炜)
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 6932881851121212536L;

    /**
     * 用户信息
     */
    private UserProfile profile;

    /**
     * 用户菜单
     */
    private List<MenuVo> menus;

    /**
     * 操作权限
     */
    private UserPerm perms;

    public UserInfo() {

    }

    public UserInfo(UserProfile profile, List<MenuVo> userMenus) {
        this.profile = profile;
        this.menus = userMenus;
    }

    public UserInfo(UserProfile profile, List<MenuVo> userMenus, UserPerm perms) {
        this.profile = profile;
        this.menus = userMenus;
        this.perms = perms;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public List<MenuVo> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuVo> menus) {
        this.menus = menus;
    }

    public UserPerm getPerms() {
        return perms;
    }

    public void setPerms(UserPerm perms) {
        this.perms = perms;
    }
}

package com.mobvista.okr.dto.mbacs;

import java.io.Serializable;
import java.util.List;

/**
 * 注释：账号中心 -- 用户信息
 * @author Weier Gu (顾炜)
 */
public class LoginAccountUserInfo implements Serializable {

    private static final long serialVersionUID = -3079155753376770146L;

    /**
     * 用户信息
     */
    private AccountUserProfile profiles;
    /**
     * 用户权限
     */
    private Permission permissions;

    public AccountUserProfile getProfiles() {
        return profiles;
    }

    public void setProfiles(AccountUserProfile profiles) {
        this.profiles = profiles;
    }

    public Permission getPermissions() {
        return permissions;
    }

    public void setPermissions(Permission permissions) {
        this.permissions = permissions;
    }

    /**
     * 领导信息
     */
    public static class LeaderDetail {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Permission {
        private List<String> all;
        private List<Item> group;

        public List<String> getAll() {
            return all;
        }

        public void setAll(List<String> all) {
            this.all = all;
        }

        public List<Item> getGroup() {
            return group;
        }

        public void setGroup(List<Item> group) {
            this.group = group;
        }
    }

    /**
     * 用户角色
     */
    public static class Item {
        //角色名称
        private String name;
        //子角色
        private List<Item> items;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }
    }

}

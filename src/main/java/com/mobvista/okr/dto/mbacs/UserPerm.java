package com.mobvista.okr.dto.mbacs;

import java.util.List;
import java.util.Map;

/**
 * 注释：用户权限对象
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-23 下午11:32
 */
public class UserPerm {
    private List<MenuVo> menuVos;
    private List<String> apiPerms;
    private List<String> operPerms;
    private Map<String, String> dataPerm;

    public static UserPerm instance(List<MenuVo> menuVos, List<String> apiPerms, List<String> operPerms, Map<String, String> dataPerm) {
        UserPerm userPerm = new UserPerm();
        userPerm.setMenuVos(menuVos);
        userPerm.setApiPerms(apiPerms);
        userPerm.setOperPerms(operPerms);
        userPerm.setDataPerm(dataPerm);
        return userPerm;
    }

    public List<MenuVo> getMenuVos() {
        return menuVos;
    }

    public void setMenuVos(List<MenuVo> menuVos) {
        this.menuVos = menuVos;
    }

    public List<String> getApiPerms() {
        return apiPerms;
    }

    public void setApiPerms(List<String> apiPerms) {
        this.apiPerms = apiPerms;
    }

    public List<String> getOperPerms() {
        return operPerms;
    }

    public void setOperPerms(List<String> operPerms) {
        this.operPerms = operPerms;
    }

    public Map<String, String> getDataPerm() {
        return dataPerm;
    }

    public void setDataPerm(Map<String, String> dataPerm) {
        this.dataPerm = dataPerm;
    }
}

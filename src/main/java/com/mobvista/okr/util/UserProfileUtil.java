package com.mobvista.okr.util;

import com.alibaba.fastjson.JSONObject;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.mbacs.AccountUserProfile;

import java.util.Set;

/**
 * 注释：用户信息util工具类
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-09 下午5:04
 */
public class UserProfileUtil {
    /**
     * 将账户信息用户对象转换成crm用户对象
     *
     * @param accountUserProfile
     * @return
     */
    public static UserProfile getUserProfile(AccountUserProfile accountUserProfile) {
        UserProfile profile = new UserProfile();
        profile.setId(accountUserProfile.getId());
        profile.setUserName(accountUserProfile.getUsername());
        profile.setRealName(accountUserProfile.getRealName());
        profile.setStatus(accountUserProfile.getStatus());

        //设置部门
        Object departmentDetail = accountUserProfile.getDepartmentDetail();
        if (departmentDetail instanceof JSONObject) {
            JSONObject deptJsonObject = (JSONObject) departmentDetail;
            KVUtil deptUserKV = getUserKV(deptJsonObject);
            profile.setDepartmentId(deptUserKV.getId());
//            profile.setDepartment(deptUserKV.getName());

            profile.setJobName(accountUserProfile.getJob());
            profile.setEmail(accountUserProfile.getEmail());
        }

        //设置直属上级
        Object directLeaderDetail = accountUserProfile.getDirectLeaderDetail();
        if (directLeaderDetail instanceof JSONObject) {
            JSONObject leaderJsonObject = (JSONObject) directLeaderDetail;
            KVUtil leaderUserKV = getUserKV(leaderJsonObject);
            profile.setLeaderId(leaderUserKV.getId());
//            profile.setLeaderName(leaderUserKV.getName());
        }

        //设置部门领导
        Object departmentLeaderDetail = accountUserProfile.getDepartmentLeaderDetail();
        if (departmentLeaderDetail instanceof JSONObject) {
            JSONObject deptLeaderJsonObject = (JSONObject) departmentLeaderDetail;
            KVUtil deptLeaderUserKV = getUserKV(deptLeaderJsonObject);
            profile.setDepartmentId(deptLeaderUserKV.getId());
//            profile.setDeptLeaderName(deptLeaderUserKV.getName());
        }

        return profile;
    }

    /**
     * 设置user kv对象
     *
     * @param jsonObject
     * @return
     */
    private static KVUtil getUserKV(JSONObject jsonObject) {
        KVUtil userKV = new KVUtil();
        Set<String> deptLeaderKeySet = jsonObject.keySet();
        for (String key : deptLeaderKeySet) {
            Object value = jsonObject.get(key);
            userKV.setId(Long.valueOf(key));
            userKV.setName(String.valueOf(value));
        }
        return userKV;
    }
}

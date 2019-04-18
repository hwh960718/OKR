package com.mobvista.okr.service.mbacs;

import com.mobvista.okr.config.ApplicationProperties;
import com.mobvista.okr.util.AccountUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sijinzhang on 2017/3/31.
 */


@SuppressWarnings("ALL")
@Service
public class AcsProxyService {

    private final static String CODE = "UTF-8";

    @Resource
    private ApplicationProperties applicationProperties;


    @Value("${acs.accountDomain}")
    private String accountDomain;
    @Value("${acs.clientId}")
    private String clientId;
    @Value("${acs.clientSecret}")
    private String clientSecret;


    /**
     * account auth uri
     */
    public final String authUrl() {
        return this.accountDomain + "/authorize";
    }

    /**
     * account token uri
     */
    public final String tokenUrl() {
        return this.accountDomain + "/token";
    }

    /**
     * 用户退出
     */
    public final String logoutUrl() {
        return this.accountDomain + "/logout";
    }

    /**
     * 当前登录用户信息
     */
    public final String loginUserUrl() {
        return this.accountDomain + "/userinfo";
    }

    /**
     * 根据id获取用户基本信息
     */
    public final String userUrl() {
        return this.accountDomain + "/baseinfo";
    }

    /**
     * 获取部门下的所有用户
     */
    public final String deptUserUrl() {
        return this.accountDomain + "/deptuser";
    }

    /**
     * 获取部门下的所有用户
     */
    public final String deptUrl() {
        return this.accountDomain + "/department";
    }

    /**
     * 回调地址
     */
    public final String redirectUrlCallBack() {
//        return this.crmDomain + "crm/user/user/callback";
        return applicationProperties.getHost() + "/api/u/callback";
    }

    /**
     * 重定向 登录
     */
    public final String redirectUrlLogin() {
        return applicationProperties.getHost() + "/api/u/login";
    }


    /**
     * 重定向 acs退出跳转到登录页
     *
     * @return
     */
    public final String redirectUrlLoginOut2login() {
        return logoutUrl() + "?state=code&client_id=" + getClientId() + "&redirect_uri=" + redirectUrlLogin();
    }


    public String getAcsRequestArgs() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("client_id", clientId);
        String signature = AccountUtil.getSignature(map, clientSecret);
        String params = "client_id=" + clientId + "&sign=" + signature;
        return params;
    }


    public String getTokenParams(String code) {
        String tokenParams = "client_id=" + clientId + "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUrlEncode() + "&grant_type=authorization_code";
        return "code=" + code + "&" + tokenParams;
    }


    public String getAcsUserURIByUserId(String userId) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("id", userId);
        params.put("client_id", clientId);
        String signature = AccountUtil.getSignature(params, clientSecret);
        return userUrl() + "?id=" + userId + "&client_id=" + clientId + "&sign=" + signature;
    }


    public String getAcsDeptURIByDeptId(Long departmentId) throws IOException {
        return getAcsDeptURIByDeptIds(String.valueOf(departmentId), 1);
    }

    /**
     * 根据部门id获取部门下的人员（不包括子级部门）
     *
     * @param departmentIds
     * @return
     * @throws IOException
     */

    public String getAcsDeptURIByDeptIds(String departmentIds, int page) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("dept_id", departmentIds);
        params.put("page", page);
        params.put("client_id", clientId);
        String signature = AccountUtil.getSignature(params, clientSecret);
        return deptUserUrl() + "?dept_id=" + departmentIds + "&page=" + page + "&client_id=" + clientId + "&sign=" + signature;
    }

    /**
     * 获取回调uri
     */
    public final String redirectUrlEncode() {
        try {
            return URLEncoder.encode(redirectUrlCallBack(), CODE);
        } catch (Exception e) {
            return URLEncoder.encode(redirectUrlCallBack());
        }
    }


    public String getClientId() {
        return clientId;
    }


    public String getClientSecret() {
        return clientSecret;
    }

}

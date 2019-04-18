package com.mobvista.okr.service.mbacs;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.constants.UserLoginConstants;
import com.mobvista.okr.domain.UserProfile;
import com.mobvista.okr.dto.mbacs.*;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.util.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释：登录service
 * 作者：刘腾飞【liutengfei】
 * 时间：2016-12-22 下午6:09
 */
@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    @Resource
    private UserCacheService userCacheService;

    @Resource
    private AcsProxyService acsProxyService;


    /**
     * 用户登录回调处理
     *
     * @param code
     * @param response
     */
    public void callback(String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取用户信息
        UserInfo userInfo = getUserInfo(code, request);
        //设置用户session
        userCacheService.setUserInfo(userInfo, UserLoginConstants.USER_EXPIRE);
        //设置用户cookie
        setUserCookie(response, userInfo);
    }


    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public UserInfo getUserInfo(Long userId) {
        return userCacheService.getUserInfo(userId);
    }

    /**
     * 用户退出
     *
     * @param response
     */
    public void logout(HttpServletResponse response) {
        //删除cookie
        Cookie cookie = new Cookie(UserLoginConstants.USER_COOKIE_NAME, null);
        cookie.setPath(UserLoginConstants.USER_COOKIE_PATH);
        cookie.setDomain(UserLoginConstants.USER_COOKIE_DOMAIN);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        try {
            Long currentUserId = SecurityUtils.getCurrentUserId();
            //删除session
            userCacheService.delUserInfo(currentUserId);
            //删除用户权限缓存
            userCacheService.delCacheUserPerm(currentUserId);
            //删除用户登录信息
            userCacheService.delUserLoginTime(currentUserId);
        } catch (Exception e) {
            log.error("用户退出清除用户信息异常", e);
        }


    }

    /**
     * 获取用户权限缓存信息
     *
     * @param userId
     * @return
     */
    public UserPerm getUserPerm(Long userId) {
        return userCacheService.getCacheUserPerm(userId);
    }


    /**
     * 获取登录缓存
     *
     * @param userId
     * @return
     */
    public String getLoginCache(Long userId) {
        return userCacheService.getUserLoginTime(userId);
    }


    /**
     * 重置缓存
     *
     * @param userId
     */
    public void resetLoginCache(Long userId) {
        //更新用户权限缓存
        userCacheService.setCacheUserPermExpire(userId);
        //更新用户会话时间
        userCacheService.setUserLoginTime(userId, System.currentTimeMillis());
    }


    /**
     * 获取用户信息
     *
     * @param code
     * @param request
     * @return
     * @throws Exception
     */
    private UserInfo getUserInfo(String code, HttpServletRequest request) throws Exception {
        String accessToken = getAccessToken(code);
        LoginAccountUserInfo loginAccountUserInfo = getAccountUserInfo(accessToken);
        if (loginAccountUserInfo == null) {
            throw new RuntimeException("获取用户信息异常");
        }
        AccountUserProfile accountUserProfile = loginAccountUserInfo.getProfiles();
        UserProfile userProfile = UserProfileUtil.getUserProfile(accountUserProfile);

        //获取权限
        List<String> menus = new ArrayList<>();
        List<String> apiPerms = new ArrayList<>();
        List<String> operPerms = new ArrayList<>();
        Map<String, String> dataPerm = new HashMap<>();
        LoginAccountUserInfo.Permission permissions = loginAccountUserInfo.getPermissions();
        List<String> routeList = permissions.getAll();
        for (String route : routeList) {
            if (route.indexOf("portal_") == 0) {
                String menu = route.substring("portal_".length(), route.length());
                menus.add(menu);
            }
            if (route.indexOf("/") == 0) {
                apiPerms.add(route);
            }
            if (route.indexOf("oper_") == 0) {
                operPerms.add(route);
            }
            if (route.indexOf("data_perm_") == 0) {
                dataPerm.put(route, route);
            }
        }

        //获取格式化后的菜单
        List<MenuVo> userMenus = getFormatMenus(menus, request);

        UserPerm userPerm = UserPerm.instance(userMenus, apiPerms, operPerms, dataPerm);
        userCacheService.setCacheUserPerm(userProfile.getId(), userPerm);
        return new UserInfo(userProfile, userMenus, userPerm);
    }


    /**
     * 设置用户cookie
     *
     * @param response
     * @param userInfo
     * @throws Exception
     */
    private void setUserCookie(HttpServletResponse response, UserInfo userInfo) throws Exception {
        //保存用户登录时间
        UserProfile profile = userInfo.getProfile();
        Long id = profile.getId();
        String username = profile.getRealName();
        Long now = DateUtil.nowMillis();
        //更新用户登录时间
        userCacheService.setUserLoginTime(id, now);
        //设置cookie
        String token = TokenUtil.getUserToken(id, username, userInfo.getPerms().getOperPerms(), now);
        Cookie cookie = new Cookie(UserLoginConstants.USER_COOKIE_NAME, token);
        cookie.setPath(UserLoginConstants.USER_COOKIE_PATH);
        cookie.setDomain(UserLoginConstants.USER_COOKIE_DOMAIN);
        response.addCookie(cookie);
    }

    /**
     * 获取access token
     *
     * @return
     * @throws Exception
     */
    private String getAccessToken(String code) throws Exception {
        String tokenParams = acsProxyService.getTokenParams(code);
        String data = OkHttpUtil.post(acsProxyService.tokenUrl(), OkHttpUtil.FORM, tokenParams);
        if (data.contains("access_token")) {
            AccessTokenResult result = JSON.parseObject(data, AccessTokenResult.class);
            return result.getAccessToken();
        } else {
            log.error("调用账户中心获取access token错误，返回结果：" + data);
            return null;
        }
    }

    /**
     * 获取账号中心用户信息
     *
     * @param accessToken
     * @return
     * @throws Exception
     */
    private LoginAccountUserInfo getAccountUserInfo(String accessToken) throws Exception {
        String userParams = "access_token=" + accessToken + "&fields=" + AccountUtil.USER_FIELDS;
        String userURI = acsProxyService.loginUserUrl() + "?" + userParams;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(userURI).build();
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (response.code() == 200 && null != body) {
            String string = body.string();
            if (string.contains("profiles")) {
                return JSON.parseObject(string, LoginAccountUserInfo.class);
            }
        }
        log.error("调用账户中心获取用户信息错误，响应结果：" + response.toString() + "，返回结果：" + JSON.toJSONString(body));
        return null;
    }

    /**
     * 获取格式化后的菜单列表
     *
     * @param routeList
     * @param request
     * @return
     */
    private List<MenuVo> getFormatMenus(List<String> routeList, HttpServletRequest request) {
        List<MenuVo> validMenuVoList = new ArrayList<>();

        // 获取菜单列表，支持国际化
        List<MenuVo> menuVoList = MenuUtil.allMenus();

        for (MenuVo menuVo : menuVoList) {
            String url = menuVo.getOperUrl();
            //如果权限带错不包含当前操作权限，跳过
            //操作权限为空，则不需要授权
            if (StringUtils.isNotBlank(url) && !routeList.contains(url)) {
                continue;
            }
            validMenuVoList.add(menuVo);
        }
        List<MenuVo> treeMenuList = new ArrayList<>();
        return getTreeMenus(treeMenuList, validMenuVoList, 1);
    }

    /**
     * 获取菜单树
     *
     * @param treeMenuList
     * @param allMenuVoList
     * @param level
     * @return
     */
    private List<MenuVo> getTreeMenus(List<MenuVo> treeMenuList, List<MenuVo> allMenuVoList, int level) {
        List<MenuVo> levelList = new ArrayList<>();
        for (MenuVo menuVo : allMenuVoList) {
            if (menuVo.getLevel() == level) {
                levelList.add(menuVo);
            }

        }
        if (levelList.size() == 0) {
            return treeMenuList;
        }

        for (MenuVo levelMenuVo : levelList) {
            String parentCode = levelMenuVo.getParentCode();
            if (level == 1) {
                treeMenuList.add(levelMenuVo);
                continue;
            }
            for (MenuVo menuVo : treeMenuList) {
                String code = menuVo.getCode();
                if (!code.equals(parentCode)) {
                    continue;
                }
                List<MenuVo> menuVos = menuVo.getChildren();
                if (menuVos == null) {
                    menuVos = new ArrayList<>();
                }
                menuVos.add(levelMenuVo);
                menuVo.setChildren(menuVos);
            }
        }
        return getTreeMenus(treeMenuList, allMenuVoList, level + 1);
    }

}

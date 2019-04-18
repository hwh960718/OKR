package com.mobvista.okr.web.rest;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.constants.UserLoginConstants;
import com.mobvista.okr.dto.mbacs.MenuVo;
import com.mobvista.okr.dto.mbacs.UserInfo;
import com.mobvista.okr.security.SecurityUtils;
import com.mobvista.okr.service.mbacs.AcsProxyService;
import com.mobvista.okr.service.mbacs.LoginService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/api/u")
public class AuthResource {
    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

    @Resource
    private LoginService loginService;

    @Resource
    private AcsProxyService acsProxyService;

    @Resource
    private WebApplicationContext applicationContext;

    /**
     * 检查登陆状态
     *
     * @param request the HTTP request
     * @return
     */
    @ApiOperation(value = "检查登陆状态")
    @ResponseBody
    @GetMapping("/authenticate")
    public CommonResult isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return CommonResult.success(request.getRemoteUser());
    }

    /**
     * acs登陆
     *
     * @return
     */
    @ApiOperation(value = "acs登陆")
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("REST request to login system");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String loginUrl = acsProxyService.authUrl() + "?response_type=code&state=code&client_id=" + acsProxyService.getClientId() + "&redirect_uri=" + acsProxyService.redirectUrlCallBack();
        return "redirect:" + loginUrl;
    }

    /**
     * acs登陆回调
     *
     * @param code
     * @param response
     * @return
     */
    @ApiOperation(value = "acs登陆回调")
    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletRequest request, HttpServletResponse response) {
        log.debug("REST request to login callback");
        try {

            if (code == null || code.length() == 0) {
                return JSON.toJSONString(CommonResult.error("缺少code"));
            }
            //回调处理
            loginService.callback(code, request, response);
            return "redirect:http://" + UserLoginConstants.USER_COOKIE_DOMAIN + "/";
        } catch (Exception e) {
            log.error("账号中心回调处理异常", e);
            return JSON.toJSONString(CommonResult.error());
        }
    }

    /**
     * acs注销
     *
     * @param response
     * @return
     */
    @ApiOperation(value = "acs注销")
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        log.debug("REST request to logout system");
        try {
            loginService.logout(response);
        } catch (Exception e) {
            log.error("用户退出异常", e);
        }
        //账号中心退出跳转至登录
        return "redirect:" + acsProxyService.redirectUrlLoginOut2login();

    }


    /**
     * 获取用户授权菜单
     *
     * @return
     */
    @RequestMapping(value = "/menuList", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getUserInfo() {
        try {
            UserInfo userInfo = loginService.getUserInfo(SecurityUtils.getCurrentUserId());
            if (userInfo == null) {
                return CommonResult.reLogin();
            }
            List<MenuVo> menus = userInfo.getMenus();
            if (null == menus || menus.size() == 0) {
                //无访问权限如何处理
            }
            return CommonResult.success(menus);
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return CommonResult.error(e);
        }

    }


    /**
     * 该方法不对外开放
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/getAllUrl")
    public CommonResult getAllUrl() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<String> urlList = new ArrayList<>();
        String listStr = "";
        for (RequestMappingInfo info : map.keySet()) {
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            urlList.addAll(patterns);
        }
        System.out.println(JSON.toJSONString(urlList));
        return CommonResult.success(urlList);
    }


}

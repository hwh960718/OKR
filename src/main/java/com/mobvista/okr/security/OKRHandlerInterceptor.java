package com.mobvista.okr.security;

import com.alibaba.fastjson.JSON;
import com.mobvista.okr.common.CommonResult;
import com.mobvista.okr.dto.mbacs.UserPerm;
import com.mobvista.okr.service.mbacs.AcsProxyService;
import com.mobvista.okr.service.mbacs.LoginService;
import com.mobvista.okr.util.CookieUtil;
import com.mobvista.okr.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * OKR拦截器
 *
 * @author 顾炜(GUWEI) 时间：2018/5/3 17:01
 */
public class OKRHandlerInterceptor implements HandlerInterceptor {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(OKRHandlerInterceptor.class);

    @Resource
    private LoginService loginService;

    @Resource
    private AcsProxyService acsProxyService;


    private BeanFactory factory;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        getService(request);
        String currentUrl = request.getRequestURI();
        String token = CookieUtil.getCookieByName(request, CookieUtil.AUTH_NAME);
        if (StringUtils.isBlank(token)) {
            log.warn("OKR拦截器。token不存在。URL:{}", currentUrl);
            CommonResult result = CommonResult.reLogin();
            result.setData(acsProxyService.redirectUrlLoginOut2login());
            makeResponseData(response, result);
            return false;
        }

        //校验token是否正确
        if (!TokenUtil.validateToken(token)) {
            log.warn("OKR拦截器.token 无效");
            CommonResult result = CommonResult.reLogin();
            result.setData(acsProxyService.redirectUrlLoginOut2login());
            makeResponseData(response, result);
            return false;
        }
        Long currentUserId;
        String userName;
        try {
            currentUserId = TokenUtil.getUserIdByToken(token);
            userName = TokenUtil.getUserNameByToken(token);
        } catch (Exception e) {
            log.warn("OKR拦截器.从token获取用户id失败，用户token无效");
            CommonResult result = CommonResult.reLogin();
            result.setData(acsProxyService.redirectUrlLoginOut2login());
            makeResponseData(response, result);
            return false;
        }
        //校验会话是否超时
        String loginTime = loginService.getLoginCache(currentUserId);
        if (StringUtils.isBlank(loginTime)) {
            log.warn("OKR拦截器.登录超时，用户id={},userName={}", currentUserId, userName);
            CommonResult result = CommonResult.reLogin();
            result.setData(acsProxyService.redirectUrlLoginOut2login());
            makeResponseData(response, result);
            return false;
        }

        UserPerm userPerm = loginService.getUserPerm(currentUserId);
        if (null == userPerm) {
            log.warn("OKR拦截器.权限校验。用户权限不存在。当前用户ID:{},userName={},URL:{}", currentUserId, userName, currentUrl);
            makeResponseData(response, CommonResult.noAuthority());
            return false;
        }

        //校验api权限
        List<String> apiPerms = userPerm.getApiPerms();
        if (null == apiPerms || apiPerms.size() == 0) {
            log.warn("OKR拦截器.权限校验。用户无任何访问权限！当前用户ID:{},userName={},URL:{}", currentUserId, userName, currentUrl);
            makeResponseData(response, CommonResult.noAuthority());
            return false;

        }
        if (!apiPerms.contains(currentUrl)) {
            log.warn("OKR拦截器，权限校验。用户无此URL访问权限！当前用户ID:{},userName={},URL:{}", currentUserId, userName, currentUrl);
            makeResponseData(response, CommonResult.noAuthority());
            return false;
        }

        //重置用户缓存
        loginService.resetLoginCache(currentUserId);
        return true;
    }

    private void getService(HttpServletRequest request) {
        getBeanFactory(request);
        if (null == loginService) {
            loginService = factory.getBean(LoginService.class);
        }
        if (null == acsProxyService) {
            acsProxyService = factory.getBean(AcsProxyService.class);
        }
    }

    private void getBeanFactory(HttpServletRequest request) {
        if (null == factory) {
            factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void makeResponseData(HttpServletResponse response, CommonResult commonResult) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (OutputStream out = response.getOutputStream()) {
            //将数据写到response body中
            out.write(JSON.toJSONString(commonResult).getBytes());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.mobvista.okr.config;

import com.mobvista.okr.security.OKRHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author 顾炜(GUWEI) 时间：2018/5/3 17:04
 */
//@Configuration
public class MyWebAppConfigurer extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        //需要排除的拦截
        String[] excludePath =
                {
                        "/app/**/*.{js,html}",
                        "/bower_components/**",
                        "/i18n/**",
                        "/content/**",
                        "/swagger-ui.html",
                        "/test/**",
                        "/api/u/callback",
                        "/api/u/logout",
                        "/api/u/login",
                        "/api/u/getAllUrl",
                        "/api/tools/**",
                        "/doc/**",
                        "/error"
                };
        registry.addInterceptor(new OKRHandlerInterceptor())
                .excludePathPatterns(excludePath)
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}

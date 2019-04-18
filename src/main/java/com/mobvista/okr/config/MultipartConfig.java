package com.mobvista.okr.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * 描述：
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/31 12:42
 */
@Configuration
public class MultipartConfig {

    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //上传文件临时目录配置
        String location = "/data/tmp/";
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}

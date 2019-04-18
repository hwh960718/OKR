package com.mobvista.okr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author 顾炜[GuWei]
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String initSecret;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用host
     */
    private String host;

    /**
     * 应用cookieDomain
     */
    private String cookieDomain;

    /**
     * 检查订单秘钥
     */
    private boolean checkOrderPassword = false;

    public String getInitSecret() {
        return initSecret;
    }

    public void setInitSecret(String initSecret) {
        this.initSecret = initSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public boolean isCheckOrderPassword() {
        return checkOrderPassword;
    }

    public void setCheckOrderPassword(boolean checkOrderPassword) {
        this.checkOrderPassword = checkOrderPassword;
    }
}

package com.mobvista.okr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：邮箱配置
 *
 * @author Weier Gu (顾炜)
 * @date 2018/10/15 11:25
 */
@Configuration
@ConfigurationProperties(prefix = "mail")
public class EmailProperties {

    /**
     * 邮件服务器host
     */
    private String host;
    /**
     * 邮件服务器 发送人
     */
    private String sender;
    /**
     * 邮件服务器 发送人名称
     */
    private String senderName;
    /**
     * 邮件服务器 用户名
     */
    private String userName;
    /**
     * 邮件服务器 密码
     */
    private String password;
    /**
     * 邮件服务器 端口
     */
    private String port;
    /**
     * 邮件 接收人
     */
    private String receiveMailAddress;
    /**
     * 邮件零时文件路径
     */
    private String tempFilePath;

    /**
     * 是否是测试
     */
    private boolean test;

    /**
     * 是否发送邮件
     */
    private boolean emailEnable = false;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getReceiveMailAddress() {
        return receiveMailAddress;
    }

    public void setReceiveMailAddress(String receiveMailAddress) {
        this.receiveMailAddress = receiveMailAddress;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isEmailEnable() {
        return emailEnable;
    }

    public void setEmailEnable(boolean emailEnable) {
        this.emailEnable = emailEnable;
    }
}


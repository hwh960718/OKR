package com.mobvista.okr.config;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.UploadManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiahuijie on 16/7/12.
 */
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuConfig {

    private String ak;
    private String sk;
    private String bucket;
    private String host;

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * 配置图片验证码
     */
    @Bean
    public UploadManager uploadManager() throws QiniuException {
        com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration();
        UploadManager uploadManager = new UploadManager(configuration);
        return uploadManager;
    }
}
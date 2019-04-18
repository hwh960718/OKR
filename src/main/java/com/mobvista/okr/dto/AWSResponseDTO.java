package com.mobvista.okr.dto;

import java.io.Serializable;

/**
 * Created by jiahuijie on 2017/4/21.
 */
public class AWSResponseDTO implements Serializable {
    /**
     * url
     */
    public String url;

    /**
     * 文件名
     */
    public String fileName;

    /**
     * aws 对应key
     */
    public String key;

    public AWSResponseDTO() {
    }

    public AWSResponseDTO(String url) {
        this.url = url;
    }

    public AWSResponseDTO(String url, String key) {
        this.url = url;
        this.fileName = key;
    }

    public AWSResponseDTO(String url, String fileName, String key) {
        this.url = url;
        this.fileName = fileName;
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "AWSResponseDTO{" +
                "url='" + url + '\'' +
                '}';
    }
}

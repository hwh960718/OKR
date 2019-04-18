package com.mobvista.okr.util;


import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;


/**
 * 注释：ok http工具类
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-03-08 下午3:11
 */
public class OkHttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkHttpUtil.class);

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    /**
     * 同步get
     *
     * @param url 请求url
     * @return
     * @throws Exception
     */
    public static String get(String url) throws Exception {
        Request.Builder builder = new Request.Builder();
        //设置请求头
//        setRequestHeader(APIUtil.getTokenHeader(), builder);
        Request request = builder.url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            LOGGER.error("请求异常：url:" + url + "，请求返回结果：" + response.body().string());
            throw new RuntimeException();
        }
    }


    /**
     * 同步get
     *
     * @param url 请求url
     * @return
     * @throws Exception
     */
    public static InputStream getInputStream(String url) throws Exception {
        Request.Builder builder = new Request.Builder();
        //设置请求头
//        setRequestHeader(APIUtil.getTokenHeader(), builder);
        Request request = builder.url(url).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().byteStream();
        } else {
            LOGGER.error("请求异常：url:" + url + "，请求返回结果：" + response.body().string());
            throw new RuntimeException();
        }
    }

    /**
     * 异步get请求
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static void asynGet(String url) throws Exception {
        Request.Builder builder = new Request.Builder();
        //设置请求头
//        setRequestHeader(APIUtil.getTokenHeader(), builder);
        Request request = builder.url(url).build();
        enqueue(request);
    }

    /**
     * 同步post
     *
     * @param url
     * @param mediaType
     * @param data
     * @return
     * @throws IOException
     */
    public static String post(String url, MediaType mediaType, String data) throws Exception {
        RequestBody body = RequestBody.create(mediaType, data);
        Request.Builder builder = new Request.Builder();
        //设置请求头
//        setRequestHeader(APIUtil.getTokenHeader(), builder);
        Request request = builder.url(url).post(body).build();
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            LOGGER.error("请求异常：url:" + url + "，请求返回结果：" + response.body().string());
            throw new RuntimeException();
        }
    }

    /**
     * 异步post json
     *
     * @param url
     * @param data
     * @throws IOException
     */
    public static void asynPost(String url, MediaType mediaType, String data) throws Exception {
        Request.Builder builder = new Request.Builder();
        //设置请求头
//        setRequestHeader(APIUtil.getTokenHeader(), builder);

        RequestBody body = RequestBody.create(mediaType, data);
        Request request = builder.url(url).post(body).build();
        enqueue(request);
    }

    /**
     * 设置请求投
     *
     * @param headers
     * @param builder
     */
    private static void setRequestHeader(Map<String, String> headers, Request.Builder builder) {
        if (headers == null || headers.size() == 0) {
            return;
        }
        Set<Map.Entry<String, String>> entrySet = headers.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.addHeader(key, value);
        }
    }


    /**
     * 通用同步请求。
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static Response execute(Request request) throws IOException {
        return OK_HTTP_CLIENT.newCall(request).execute();
    }

    /**
     * 通用异步请求
     *
     * @param request
     * @param responseCallback
     */
    private static void enqueue(Request request, Callback responseCallback) {
        OK_HTTP_CLIENT.newCall(request).enqueue(responseCallback);
    }

    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     *
     * @param request
     */
    private static void enqueue(Request request) {
        OK_HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}

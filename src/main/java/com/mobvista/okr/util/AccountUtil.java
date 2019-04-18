package com.mobvista.okr.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 注释：账号中心配置
 * 作者：刘腾飞【liutengfei】
 * 时间：2017-01-07 下午2:13
 */
public class AccountUtil {

    //需要获取用户哪些信息
    public final static String USER_FIELDS = "profiles,permissions";


    /**
     * 签名生成算法
     *
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @param secret 签名密钥
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(Map<String, Object> params, String secret) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, Object> sortedParams = new TreeMap<String, Object>(params);
        Set<Map.Entry<String, Object>> entrys = sortedParams.entrySet();

        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        for (Map.Entry<String, Object> param : entrys) {
            basestring.append(param.getValue());
        }
        basestring.append(secret);
        // 使用MD5对待签名串求签
        byte[] bytes = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("SHA-1");
            bytes = md5.digest(basestring.toString().getBytes("UTF-8"));
        } catch (GeneralSecurityException ex) {
            throw new IOException(ex);
        }
        // 将MD5输出的二进制结果转换为小写的十六进制
        return toHexString(bytes);
    }

    /**
     * 将字节流转换成16进制字符串
     *
     * @param bytes 字节流
     * @return 16进制字符串
     */
    public static String toHexString(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex);
        }
        return sign.toString();
    }
}

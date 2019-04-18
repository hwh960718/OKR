package com.mobvista.okr.util;

import com.mobvista.okr.constants.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 描述：md5 加密
 *
 * @author Weier Gu (顾炜)
 * @date 2018/9/28 10:39
 */
public class MD5Utils {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(MD5Utils.class);


    /**
     * md5加密默认key
     */
    private static final String MD5_KEY = "34833a66-32ec-462d-a4f6-af36be417121";


    /**
     * 生成md5加密
     *
     * @param input  需要加密的字符串
     * @param key    入参加密key
     * @param append 加密连接符
     * @return
     */
    public static final String makeMd5(String input, String key, String append) {
        if (StringUtils.isBlank(key)) {
            key = MD5_KEY;
        }
        if (StringUtils.isBlank(append)) {
            append = CommonConstants.APPEND_2_OKR;
        }
        if (StringUtils.isNotBlank(input)) {
            return getMD5(input + append + key);
        }
        return StringUtils.EMPTY;
    }


    /**
     * 生成md5加密
     *
     * @param input 需要加密的字符串
     * @return
     */
    public static final String makeMd5(String input) {
        return makeMd5(input, null, null);
    }

    /**
     * 获取md加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            log.error("MD5加密出现错误", e);
        }
        return StringUtils.EMPTY;
    }

    public static void main(String[] args) {
        System.out.println(getMD5("1111"));
    }
}

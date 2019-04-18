package com.mobvista.okr.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注释：jwt token util
 *
 * @author 顾炜[GuWei]
 */
public class TokenUtil {

    /**
     * logger
     */
    private static final Logger log = LoggerFactory.getLogger(TokenUtil.class);
    /**
     * jwt salt
     */
    private static final String SALT = "def90c89-cb0f-4d4d-b531-90ad4fa69fd2";


    /**
     * 获取用户token
     *
     * @param id
     * @param username
     * @return
     * @throws Exception
     */
    public static String getUserToken(Long id, String username, Long logintime) throws Exception {
        Map<String, Object> headerMap = new HashMap<>();
        //默认值
        headerMap.put("typ", "JWT");
        return JWT.create().withHeader(headerMap).withClaim("id", String.valueOf(id)).withClaim("username", username).sign(Algorithm.HMAC256(SALT));
    }

    /**
     * 获取用户token
     *
     * @param id
     * @param username
     * @return
     * @throws Exception
     */
    public static String getUserToken(Long id, String username, List<String> operPerms, Long logintime) throws Exception {
        Map<String, Object> headerMap = new HashMap<>();
        //默认值
        headerMap.put("typ", "JWT");
        JWTCreator.Builder builder = JWT.create().withHeader(headerMap);
        builder.withClaim("id", String.valueOf(id))
                .withClaim("username", username);
        if (null != operPerms && operPerms.size() > 0) {
            builder.withClaim("oper", JSON.toJSONString(operPerms));
        }

        return builder.sign(Algorithm.HMAC256(SALT));
    }


    /**
     * 从token获取用户id
     *
     * @param token
     * @return
     */
    public static Long getUserIdByToken(String token) {
        if (token == null || token.length() == 0) {
            throw new RuntimeException("未发现token");
        }
        JWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim("id");
        String id = claim.asString();
        if (id == null || id.length() == 0) {
            throw new RuntimeException("token不正确，获取用户id异常");
        }
        return Long.valueOf(id);
    }


    /**
     * 是否是管理员
     *
     * @param token
     * @return
     */
    public static List<String> getUserOperByToken(String token) {
        if (token == null || token.length() == 0) {
            throw new RuntimeException("未发现token");
        }
        JWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim("oper");
        String operStr = claim.asString();
        if (operStr == null || operStr.length() == 0) {
            return null;
        }

        return JSON.parseArray(operStr, String.class);
    }


    /**
     * 从token获取用户name
     *
     * @param token
     * @return
     */
    public static String getUserNameByToken(String token) {
        JWT jwt = JWT.decode(token);
        Claim claim = jwt.getClaim("username");
        String username = claim.asString();
        if (username == null || username.length() == 0) {
            throw new RuntimeException("token不正确，获取用户名异常");
        }
        return username;
    }


    /**
     * 验证token的有效性
     *
     * @param token
     * @return true 有效 false：无效
     */
    public static boolean validateToken(String token) {
        boolean bool = true;
        try {
            Algorithm algorithm = Algorithm.HMAC256(SALT);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
            bool = false;
        } catch (JWTVerificationException exception) {
            log.error("Invalid signature/claims 签名无效");
            bool = false;
        }
        return bool;
    }


}

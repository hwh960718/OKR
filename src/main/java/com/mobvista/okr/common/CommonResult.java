package com.mobvista.okr.common;

import com.mobvista.okr.util.HTTPStatus;
import com.mobvista.okr.util.LocaleUtil;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import java.io.Serializable;

/**
 * 通用返回结果对象
 *
 * @author 顾炜[GuWei]
 */
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 862572837359274695L;

    /**
     * 返回码，0=成功，1=失败
     */
    private Integer code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    public CommonResult() {
    }

    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CommonResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功默认返回结果
     *
     * @return
     */
    public static CommonResult success() {
        return new CommonResult(HTTPStatus.OK, "success");
    }

    /**
     * 成功 -- 返回结果
     *
     * @return
     */
    public static CommonResult success(Object data) {
        return new CommonResult(HTTPStatus.OK, "success", data);
    }

    public static CommonResult success(Integer code, Object data) {
        return new CommonResult(code, "success", data);
    }

    /**
     * 失败默认返回结果
     *
     * @return
     */
    public static CommonResult error() {
        return new CommonResult(HTTPStatus.ERR, LocaleUtil.isZhForRequestLanguage() ? "系统错误，请重试" : "System exception. Please try again");
    }


    public static CommonResult warn(String msg) {
        return new CommonResult(HTTPStatus.WARN, msg);
    }

    /**
     * 失败时，根据异常类型返回结果
     *
     * @return
     */
    public static CommonResult error(Exception e) {
        return new CommonResult(HTTPStatus.ERR, e.getMessage());
    }

    /**
     * 失败默认返回结果
     *
     * @return
     */
    public static CommonResult error(String message) {
        return new CommonResult(HTTPStatus.ERR, message);
    }

    /**
     * 重新登录返回结果
     *
     * @return
     */
    public static CommonResult reLogin() {
        return new CommonResult(HTTPStatus.RELOGIN, "请重新登录");
    }

    /**
     * 重新登录返回结果
     *
     * @return
     */
    public static CommonResult reLogin(String msg) {
        return new CommonResult(HTTPStatus.RELOGIN, msg);
    }


    /**
     * 无访问权限
     *
     * @returna
     */
    public static CommonResult noAuthority() {
        return new CommonResult(HTTPStatus.NO_AUTHORITY, "无访问权限");
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static CommonResult success(String format, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, objects);
        return new CommonResult(HTTPStatus.OK, ft.getMessage());
    }

    public static CommonResult error(String format, Object... objects) {
        FormattingTuple ft = MessageFormatter.arrayFormat(format, objects);
        return new CommonResult(HTTPStatus.ERR, ft.getMessage());
    }
}

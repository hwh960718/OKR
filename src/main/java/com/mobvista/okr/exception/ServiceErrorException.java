package com.mobvista.okr.exception;


/**
 * @author guwei
 */
public class ServiceErrorException extends RuntimeException {

    private static final long serialVersionUID = 5554670489353102606L;

    public ServiceErrorException(String msg) {
        super(msg);
    }
}

package com.mobvista.okr.exception;


/**
 * @author Weier Gu (顾炜)
 */
public final class ExceptionUtil {

    public static void checkState(boolean expression, String errorMessage) {
        if (!expression) {
            throw new ServiceErrorException(errorMessage);
        }
    }
}
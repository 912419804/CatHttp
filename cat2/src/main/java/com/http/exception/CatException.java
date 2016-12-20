package com.http.exception;

/**
 * 用于全局异常处理
 */

public class CatException extends Exception {

    public enum ExceptionType {URL,TIMEOUT,JSON,SERVER,IO,FILE_NOT_FOUND,CANCEL}

    /**
     * 默认值 -1 代表java异常，因为java异常没有返回码
     * 其他为服务器返回的状态码
     */
    public int statusCode = -1;
    public ExceptionType mType;


    public CatException(int statusCode, String msg) {
        this(ExceptionType.SERVER,msg);
        this.statusCode = statusCode;
    }

    public CatException(String detailMessage) {
        super(detailMessage);
    }

    public CatException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CatException(Throwable throwable) {
        super(throwable);
    }

    public CatException(ExceptionType exceptionType, String message) {
        this(message);
        this.mType = exceptionType;
    }
}

package com.lunchinator3000.exception;

public class LunchinatorException extends RuntimeException {

    public LunchinatorException(String message) {
        super(message);
    }

    public LunchinatorException(String message, Throwable cause) {
        super(message, cause);
    }
}

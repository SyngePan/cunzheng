package com.cunzheng_01.configuration.response;

import lombok.Data;

/**
 * Created by zhangrui on 2019/7/4.
 * com.cunzheng.configuration.response
 */
@Data
public class BaseResult<T> {
    private int code;
    private String message;
    private T data = (T) new Object();

    public BaseResult() {
        this.data = (T) new Object();
    }

    public BaseResult(String msg) {
        this();
        this.code = 200;
        this.message = msg;
    }

    public BaseResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public final void returnWithoutValue(Code code) {
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    public final void returnWithValue(Code code, T object) {
        returnWithoutValue(code);
        this.data = object;
    }
}

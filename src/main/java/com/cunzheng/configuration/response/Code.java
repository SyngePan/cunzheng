package com.cunzheng_01.configuration.response;

import lombok.Getter;

/**
 * Created by zhangrui on 2019/7/4.
 * com.cunzheng.configuration.response
 */
@Getter
public enum  Code {
    //通用部分
    UNDEFINED(-1, "未定义"),
    SUCCESS(0, "成功"),

    CONTRACT_ERROR(9001,"合约调用错误"),
    SYSTEM_ERROR(9999, "系统异常，请稍后重试");

    private int code;
    private String message;

    // 构造方法
    Code(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

}

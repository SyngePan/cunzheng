package com.cunzheng.configuration.response;

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
    SYSTEM_ERROR(9999, "系统异常，请稍后重试"),

    CONTRACT_ALREADY_EXISTED(200,"合同已存在"),
    CONTRACT_NOT_EXIST(300,"合同不存在"),
	
	CODE_FILE_NOT_EXITED(400,"合同不存在"),
	CODE_FILE_MODIFIED(500,"合同被修改"),
	CODE_FILE_STATUS_ERROR(600,"合同状态错误"),
	CODE_PEMISSION_DENY(700,"权限错误");	

    private int code;
    private String message;

    // 构造方法
    Code(int code, String msg) {
        this.code = code;
        this.message = msg;
    }

}

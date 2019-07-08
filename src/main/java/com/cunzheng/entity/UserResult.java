package com.cunzheng.entity;

public enum UserResult {
    INVALID_USERNAME("invalid username"),
    INVALID_PASSWORD("invalid password"),
    SUCCESS("success");

    private String message;

    UserResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


}

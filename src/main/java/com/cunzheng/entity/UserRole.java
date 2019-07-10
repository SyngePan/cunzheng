package com.cunzheng.entity;

public enum UserRole {
    LANDLORD(1), TENANT(2);

    private int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

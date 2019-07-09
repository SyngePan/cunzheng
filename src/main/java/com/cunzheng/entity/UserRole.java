package com.cunzheng.entity;

public enum UserRole {
    OWNER(1), RENTER(2);

    private int code;

    UserRole(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

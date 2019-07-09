package com.cunzheng.entity;

public class UserThreadLocal {
    private UserThreadLocal(){

    }

    private static final ThreadLocal<UserBean> local = new ThreadLocal<UserBean>();

    public static void set(UserBean user){
        local.set(user);;
    }

    public static UserBean get(){
        return local.get();
    }
}

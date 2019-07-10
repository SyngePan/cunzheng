package com.cunzheng.Interceptor;

import com.cunzheng.entity.UserRole;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserEntitlement {
    UserRole[] value();
}

package com.cunzheng.Interceptor;

import com.cunzheng.entity.UserBean;
import com.cunzheng.servie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor extends HandlerInterceptorAdapter {
    private ThreadLocal<UserBean> userBeanThreadLocal = new ThreadLocal<>();

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        UserBean userBean = userService.verifyUserNameAndPassWord(userName, password);
        if(userBean!=null){
            userBeanThreadLocal.set(userBean);
            return true;
        }else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,"用户名或者密码错误");
            return false;
        }
    }
}
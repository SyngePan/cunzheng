package com.cunzheng.Interceptor;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.servie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginUserInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        UserBean userBean = userService.verifyUserNameAndPassWord(userName, password);
        if (userBean != null) {
            UserThreadLocal.set(userBean);
            return true;
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "invalid username or password.");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserThreadLocal.set(null);
    }
}
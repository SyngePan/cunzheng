package com.cunzheng.Interceptor;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.servie.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class LoginUserInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        UserBean userBean = userService.verifyUserNameAndPassWord(userName, password);
        //user not exists with the user name and password
        if (userBean == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid username or password.");
            return false;
        }

        //check method annotation to verify the access
        if (handler instanceof HandlerMethod) {
            Method method = ((HandlerMethod) handler).getMethod();
            UserEntitlement annotation = method.getAnnotation(UserEntitlement.class);
            //no annotation found, pass
            if (annotation == null) {
                UserThreadLocal.set(userBean);
                return true;
            }

            //annotation contains, pass
            for(int i = 0;i<annotation.value().length;i++){
                if(userBean.getUserRole().equals(annotation.value()[i])){
                    UserThreadLocal.set(userBean);
                    return true;
                }
            }
        }else{
            UserThreadLocal.set(userBean);
            return true;
        }
        //otherwise, access denied
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");
        return false;

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        UserThreadLocal.set(null);
    }
}
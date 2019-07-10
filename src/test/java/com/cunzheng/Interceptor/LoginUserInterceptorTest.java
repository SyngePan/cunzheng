package com.cunzheng.Interceptor;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginUserInterceptorTest {
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @Autowired
    private LoginUserInterceptor loginUserInterceptor;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        UserBean userBean = userRepository.findByUserName(USERNAME);
        if (userBean != null) {
            userRepository.delete(userBean);
        }
        String encodePassword = new String(DigestUtils.md5Digest(PASSWORD.getBytes()));
        userBean = new UserBean(USERNAME, encodePassword, UserRole.LANDLORD, "{}");
        userRepository.save(userBean);
    }

    @After
    public void tearDown() {
        UserBean userBean = userRepository.findByUserName(USERNAME);
        userRepository.delete(userBean);
    }

    @Test
    public void preHandleInvalidPassword() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handler = mock(HandlerMethod.class);

        when(request.getParameter("username")).thenReturn(USERNAME);
        when(request.getParameter("password")).thenReturn("test1");

        //when
        boolean result = loginUserInterceptor.preHandle(request, response, handler);

        //then
        assertFalse(result);
    }

    @Test
    public void preHandleInvalidUsername() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handler = mock(HandlerMethod.class);

        when(request.getParameter("username")).thenReturn("test1");
        when(request.getParameter("password")).thenReturn(PASSWORD);

        //when
        boolean result = loginUserInterceptor.preHandle(request, response, handler);

        //then
        assertFalse(result);
    }

    @Test
    public void preHandleAnnotationNull() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handler = mock(HandlerMethod.class);
        Method method = mock(Method.class);

        when(request.getParameter("username")).thenReturn(USERNAME);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(handler.getMethod()).thenReturn(method);

        //when
        boolean result = loginUserInterceptor.preHandle(request, response, handler);

        //then
        assertTrue(result);
    }

    @Test
    public void preHandleAnnotationContains() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handler = mock(HandlerMethod.class);
        Method method = mock(Method.class);
        UserEntitlement userEntitlement = mock(UserEntitlement.class);

        when(request.getParameter("username")).thenReturn(USERNAME);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(handler.getMethod()).thenReturn(method);
        when(method.getAnnotation(UserEntitlement.class)).thenReturn(userEntitlement);

        when(userEntitlement.value()).thenReturn(new UserRole[]{UserRole.LANDLORD});

        //when
        boolean result = loginUserInterceptor.preHandle(request, response, handler);

        //then
        assertTrue(result);
    }

    @Test
    public void preHandleAnnotationNotContains() throws Exception {
        //given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerMethod handler = mock(HandlerMethod.class);
        Method method = mock(Method.class);
        UserEntitlement userEntitlement = mock(UserEntitlement.class);

        when(request.getParameter("username")).thenReturn(USERNAME);
        when(request.getParameter("password")).thenReturn(PASSWORD);
        when(handler.getMethod()).thenReturn(method);
        when(method.getAnnotation(UserEntitlement.class)).thenReturn(userEntitlement);

        when(userEntitlement.value()).thenReturn(new UserRole[]{UserRole.TENANT});

        //when
        boolean result = loginUserInterceptor.preHandle(request, response, handler);

        //then
        assertFalse(result);
    }
}
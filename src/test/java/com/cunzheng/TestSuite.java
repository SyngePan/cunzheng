package com.cunzheng;

import com.cunzheng.Interceptor.LoginUserInterceptorTest;
import com.cunzheng.controller.UserControllerTest;
import com.cunzheng.servie.UserServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CunzhengApplicationTest.class, UserControllerTest.class, LoginUserInterceptorTest.class, UserServiceTest.class})
public class TestSuite {
}

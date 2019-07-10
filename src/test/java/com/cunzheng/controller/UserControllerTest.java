package com.cunzheng.controller;

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

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTest {
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        clearUpTestUser();
    }

    @After
    public void tearDown() {
        clearUpTestUser();
    }

    @Test
    public void saveUser() throws Exception {
        String result = userController.saveUser(USERNAME, PASSWORD, UserRole.TENANT);
        assertEquals(result, "success");
    }

    private void clearUpTestUser() {
        UserBean userBean = userRepository.findByUserName(USERNAME);
        if (userBean != null) {
            userRepository.delete(userBean);
        }
    }
}
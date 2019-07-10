package com.cunzheng.servie;

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

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    private static final String USERNAME = "test";
    private static final String PASSWORD = "test";

    @Autowired
    private UserService userService;

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
    public void saveUser() {
        //given
        String userName = "saveUser";
        UserBean userBean = userRepository.findByUserName(userName);
        if (userBean != null) {
            userRepository.delete(userBean);
        }

        //when
        userService.saveUser(userName, "saveUser", UserRole.LANDLORD, "{}");

        //then
        userBean = userRepository.findByUserName(userName);
        assertEquals(userName, userBean.getUserName());

    }

    @Test
    public void hasUserNameCreatedYes() {
        //when
        boolean result = userService.hasUserNameCreated(USERNAME);

        //then
        assertTrue(result);
    }

    @Test
    public void hasUserNameCreatedNo() {
        //when
        boolean result = userService.hasUserNameCreated("testa");

        //then
        assertFalse(result);
    }

    @Test
    public void verifyUserNameAndPassWordPass() {
        //when
        UserBean userBean = userService.verifyUserNameAndPassWord(USERNAME, PASSWORD);

        //then
        assertEquals(USERNAME, userBean.getUserName());
    }


}
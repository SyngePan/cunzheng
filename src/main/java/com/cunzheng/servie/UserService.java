package com.cunzheng.servie;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 验证用户名是否已经被使用
     *
     * @param userName
     */
    public boolean hasUserNameCreated(String userName) {
        return userRepository.findByUserName(userName) != null;
    }

    /**
     * 验证用户名密码是否有效，有效返回用户对象
     *
     * @param userName
     * @param password
     * @return
     */
    public UserBean verifyUserNameAndPassWord(String userName, String password) {
        String encodePassword = new String(DigestUtils.md5Digest(password.getBytes()));
        UserBean userBean = userRepository.findByUserName(userName);
        if (userBean == null) {
            return null;
        } else if (!userBean.getPassword().equals(encodePassword)) {
            return null;
        }
        return userBean;
    }

    /**
     * 密码加密保存用户对象
     *
     * @param userName
     * @param password
     * @param userRole
     * @param accountJson
     */
    public void saveUser(String userName, String password, UserRole userRole, String accountJson) {
        String encodePwd = new String(DigestUtils.md5Digest(password.getBytes()));
        UserBean userBean = new UserBean(userName, encodePwd, userRole, accountJson);
        userRepository.save(userBean);
    }
}

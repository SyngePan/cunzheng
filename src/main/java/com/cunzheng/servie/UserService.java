package com.cunzheng.servie;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserResult;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean hasUserNameCreated(String userName) {
        return userRepository.findByUserName(userName) != null;
    }

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

    public void saveUser(String userName, String password, UserRole userRole, String accountJson) {
        String encodePwd = new String(DigestUtils.md5Digest(password.getBytes()));
        UserBean userBean = new UserBean(userName, encodePwd, userRole, accountJson);
        userRepository.save(userBean);
    }
}

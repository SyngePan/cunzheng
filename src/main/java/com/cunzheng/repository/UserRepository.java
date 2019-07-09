package com.cunzheng.repository;

import com.cunzheng.entity.UserBean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserBean,Long> {

    UserBean findByUserName(String userName);

}

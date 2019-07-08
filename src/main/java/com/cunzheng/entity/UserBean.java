package com.cunzheng.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class UserBean {

    @Id
    @GeneratedValue
    private int userId;
    private String userName;
    private String password;
    private UserRole userRole;
    @Column(length = 4000)
    private String accountJson;

    public UserBean(String userName, String password, UserRole userRole, String accountJson) {
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
        this.accountJson = accountJson;
    }
}

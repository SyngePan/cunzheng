package com.cunzheng.entity;

import lombok.*;

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

    private String userName;
    @Id
    @GeneratedValue
    private int userId;
    private String userHash;
    private int userRole;
    private String address;

}

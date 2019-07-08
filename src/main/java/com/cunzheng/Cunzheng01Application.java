package com.cunzheng_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class Cunzheng01Application {

    public static void main(String[] args) {
        SpringApplication.run(Cunzheng01Application.class, args);
    }

}

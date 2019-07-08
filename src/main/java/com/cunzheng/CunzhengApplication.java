package com.cunzheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CunzhengApplication {

    public static void main(String[] args) {
        SpringApplication.run(CunzhengApplication.class, args);
    }

}

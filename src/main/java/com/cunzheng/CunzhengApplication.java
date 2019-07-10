package com.cunzheng;

import com.cunzheng.entity.ContractBean;
import com.cunzheng.repository.ContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Date;

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CunzhengApplication {

    @Autowired
    private ContractRepository contractRepository;

    public static void main(String[] args) {
        SpringApplication.run(CunzhengApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("init...");
        //contractRepository.save(contractRepository.save(new ContractBean(1, "d41d8cd98f00b204e9800998ecf8427e", new Date(),
         //       "fangdong", "fangke", "d41d8cd98f00b204e9800998ecf8427e")));
    }

}

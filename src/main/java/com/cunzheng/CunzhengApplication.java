package com.cunzheng;

import com.cunzheng.contract.BlockUtil;
import net.sf.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CunzhengApplication {

    public static void main(String[] args) {
        SpringApplication.run(CunzhengApplication.class, args);
    }

    @PostConstruct
    public void init() {
        log.info("load contract address to memory...");
        //获取contract 地址加载到内存
        String json = BlockUtil.getAdminKey();
        JSONObject jsonObject = JSONObject.fromObject(json);
        String contractAddress = jsonObject.getString("contractAddress");

        BlockUtil.CONTRACT_ADDRESS = contractAddress;
    }

}

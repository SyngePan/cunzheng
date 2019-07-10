package com.cunzheng;

import com.cunzheng.contract.BlockUtil;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.repository.ContractRepository;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class CunzhengApplication {

    @Autowired
    private ContractRepository contractRepository;

    public static void main(String[] args) {
        SpringApplication.run(CunzhengApplication.class, args);
    }

    @PostConstruct
    public void init() {
        System.out.println("init...");
        //获取contract 地址加载到内存
        String json = BlockUtil.getAdminKey();
        JSONObject jsonObject = JSONObject.fromObject(json);
        String contractAddress = jsonObject.getString("contractAddress");

        BlockUtil.CONTRACT_ADDRESS = contractAddress;
    }

}

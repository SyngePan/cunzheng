package com.cunzheng_01.configuration;

import com.cunzheng_01.contract.BlockUtil;
import com.cunzheng_01.contract.CunZhengContract;
import com.cunzheng_01.contract.response.ContractDeployRet;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangrui on 2019/7/7.
 * com.cunzheng_01.configuration
 */
@Slf4j
@Component
public class listener implements CommandLineRunner {


    @Autowired
    private CunZhengContract cunZhengContract;

    @Override
    public void run(String... strings) throws Exception {
        String password = "123";
        String accountJson = BlockUtil.newAccountSM2(password);
        ContractDeployRet ret = cunZhengContract.deployContract(accountJson, password);

        if (null == ret) {
            log.error("合约部署失败");
        }
        //暂存合约 todo 数据库、缓存、文件
        BlockUtil.CONTRACT_ADDRESS = ret.getContractAddress();
        //生成私钥文件
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("accountJson", accountJson);
        accountMap.put("password", password);

        log.info("accountMap:" + accountMap.toString());
        //持久化
        BlockUtil.writeAdminKey(JSONObject.fromObject(accountMap).toString());
    }
}

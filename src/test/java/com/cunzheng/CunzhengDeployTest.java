package com.cunzheng;

import com.cunzheng.contract.BlockUtil;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractDeployRet;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CunzhengDeployTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CunZhengContract cunZhengContract;

    /**
     * 合约部署升级步骤
     * 1. 编译合约生成bin和abi
     * 2. 替换原有bin和abi文件(手动)
     * 3. 调用部署或者升级方法
     */

    @Test
    public void DeployContractTest() throws Exception {
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
        accountMap.put("contractAddress", ret.getContractAddress());

        log.info("accountMap:" + accountMap.toString());
        //持久化
        BlockUtil.writeAdminKey(JSONObject.fromObject(accountMap).toString());
    }

    @Test
    public void CompileContractTest() throws Exception {
        BlockUtil.compile("contractCode/CunZheng.sol");
    }

    @Test
    public void MaintainContractTest() throws Exception {
        String json = BlockUtil.getAdminKey();
        JSONObject jsonObject = JSONObject.fromObject(json);
        String contractAddress = jsonObject.getString("contractAddress");
        String accountJson = jsonObject.getString("accountJson");
        String password = jsonObject.getString("password");

        ContractDeployRet ret = cunZhengContract.maintainContract(accountJson, password, contractAddress);

        if (null == ret) {
            log.error("合约部署失败");
        }
        //暂存合约 todo 数据库、缓存、文件
        BlockUtil.CONTRACT_ADDRESS = ret.getContractAddress();
        //生成私钥文件
        Map<String, Object> accountMap = new HashMap<>();
        accountMap.put("accountJson", accountJson);
        accountMap.put("password", password);
        accountMap.put("contractAddress", ret.getContractAddress());

        log.info("accountMap:" + accountMap.toString());
        //持久化
        BlockUtil.writeAdminKey(JSONObject.fromObject(accountMap).toString());

    }


}


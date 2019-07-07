package com.cunzheng.controller;

import cn.hyperchain.sdk.rpc.function.FuncParamReal;
import com.cunzheng.constant.HyperchainConstant;
import com.cunzheng.contract.response.ContractDeployRet;
import com.cunzheng.contract.response.ContractInvokeRet;
import com.cunzheng.contract.service.CunZhengContractService;
import com.cunzheng.util.BlockUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangrui on 2019/6/5.
 * com.blockchain.controller
 */
@RestController
@RequestMapping("v1/contract")
@Slf4j
@Api(value = "存在合约测试", description = "存在合约测试")
public class CunZhengContractController {

    @Autowired
    private CunZhengContractService cunZhengContractService;

    private String contractAddress = "";
    private String deployContractAccountJson = "";


    @PostMapping("/deployContract")
    @ApiOperation(value = "合约部署", notes = "合约部署")
    public String deployContract() throws Exception {
        log.info("开始进行合约部署");
        deployContractAccountJson = BlockUtil.newAccountSM2(HyperchainConstant.ACCOUNT_PWD);
        ContractDeployRet contractDeployRet = cunZhengContractService.deployContract(deployContractAccountJson, HyperchainConstant.ACCOUNT_PWD);
        log.info(contractDeployRet.toString());
        log.info("合约部署成功");
        contractAddress = contractDeployRet.getContractAddress();
        return JSONObject.fromObject(contractDeployRet).toString();
    }

    @PostMapping("/saveUser")
    @ApiOperation(value = "保存用户", notes = "保存用户")
    public String saveUser() throws Exception {
        log.info("开始进行用户信息上链");
        FuncParamReal[] funcParamReals = new FuncParamReal[3];
        funcParamReals[0] = new FuncParamReal("address", HyperchainConstant.ADDRESS);
        funcParamReals[1] = new FuncParamReal("bytes32", "林波");
        funcParamReals[2] = new FuncParamReal("uint", 1);
        ContractInvokeRet saveUserRet = cunZhengContractService.callFunction(deployContractAccountJson, HyperchainConstant.ACCOUNT_PWD, contractAddress,
                "saveUser", funcParamReals, false);
        log.info("用户信息上链方法调用结果：" + saveUserRet.getReturnList().get(0));
        log.info("合约方法调用成功");
        return JSONObject.fromObject(saveUserRet).toString();
    }

    @GetMapping("/getUser")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public String getUser() throws Exception {
        log.info("开始查询链上用户信息");
        FuncParamReal[] funcParamReals = new FuncParamReal[0];
        ContractInvokeRet getUserRet = cunZhengContractService.callFunction(HyperchainConstant.ACCOUNT_JSON, HyperchainConstant.ACCOUNT_PWD, contractAddress,
                "getUser", funcParamReals, false);
        log.info("用户信息上链方法调用结果：" + getUserRet.getReturnList().get(0));
        log.info("用户姓名：" + getUserRet.getReturnList().get(1));
        log.info("用户角色：" + getUserRet.getReturnList().get(2));
        log.info("合约方法调用成功");
        return JSONObject.fromObject(getUserRet).toString();
    }
}

package com.cunzheng.controller;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.configuration.response.Code;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractInvokeRet;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * Created by zhangrui on 2019/7/7.
 * com.cunzheng_01.controller
 */
@RestController
@RequestMapping("v1/evidence")
@Slf4j
@Api(value = "电子存证", description = "电子存证模块功能")
public class CunZhengController {

    @Autowired
    private CunZhengContract cunZhengContract;
    @Autowired
    private ContractRepository contractRepository;




    @PostMapping("/saveEvidence")
    @ApiOperation(value = "文件存证", notes = "文件存证")
    public BaseResult saveEvidence(
            @ApiParam("私钥文件") @RequestParam String accountJson,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult baseResult = new BaseResult();
        System.out.println(multipartFile.getOriginalFilename());

        //md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        System.out.println("fileHash:" + hash);


        ContractInvokeRet ret = cunZhengContract.saveHash(accountJson, password,
                hash, System.currentTimeMillis());
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }


    @PostMapping("/getHash")
    @ApiOperation(value = "原件验证", notes = "文件存证")
    public BaseResult getHash(
            @ApiParam("私钥文件") @RequestParam String accountJson,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult baseResult = new BaseResult();
        System.out.println(multipartFile.getOriginalFilename());

        //md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        System.out.println("fileHash:" + hash);

        ContractInvokeRet ret = cunZhengContract.getFileByHash(accountJson, password,
                hash);
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }


    //文件哈希验证 TODO

    @PostMapping("/getFileHash")
    @ApiOperation(value = "文件哈希验证", notes = "文件哈希验证")
    public BaseResult getFileHash(
            @ApiParam("私钥文件") @RequestParam String accountJson,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件哈希") @RequestParam String fileHash
    ) throws Exception {

        BaseResult baseResult = new BaseResult();

        ContractInvokeRet ret = cunZhengContract.getFileByHash(accountJson, password,
                fileHash);
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }

    //合同状态查询
    @PostMapping("/getContract")
    @ApiOperation(value = "查询合同", notes = "查询合同")
    public BaseResult getContract(
            @ApiParam("私钥文件") @RequestParam String accountJson,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("合同编号") @RequestParam int contractId
    ) throws Exception {

        BaseResult baseResult = new BaseResult();

        ContractBean contractBean = contractRepository.findByContractId(contractId);

        if (contractBean != null && contractBean.getFileHash() != null) {
            ContractInvokeRet ret = cunZhengContract.getFileByHash(accountJson, password,
                    contractBean.getFileHash());
            baseResult.returnWithValue(Code.SUCCESS, ret);
        } else {
            baseResult.returnWithValue(Code.CONTRACT_NOT_EXIST, null);
        }

        return baseResult;
    }


    //交易哈希验证 TODO

}

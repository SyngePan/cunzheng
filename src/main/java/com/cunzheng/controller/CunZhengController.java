package com.cunzheng.controller;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.configuration.response.Code;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractInvokeRet;
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


    //交易哈希验证 TODO

}

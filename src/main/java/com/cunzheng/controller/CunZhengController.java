package com.cunzheng.controller;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.configuration.response.Code;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractInvokeRet;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.util.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;

/**
 * Created by zhangrui on 2019/7/7.
 * com.cunzheng.controller
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
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult baseResult = new BaseResult();
        log.error(multipartFile.getOriginalFilename());

        //md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.error("fileHash:" + hash);


        long currentTimeMillis = System.currentTimeMillis();
        ContractInvokeRet ret = cunZhengContract.saveHash(UserThreadLocal.get().getAccountJson(), password,
                hash, currentTimeMillis);
        baseResult.returnWithValue(Code.SUCCESS, ret);
        
        ContractBean contractBean=new ContractBean();
		contractBean.setContractId(Integer.parseInt(ret.getReturnList().get(1).toString()));
        contractBean.setContractHash(hash);
        contractBean.setUploadTime(new Date());
        contractBean.setLandlordSignature(null);
        contractBean.setTenantSignature(null);
        contractBean.setFileHash(hash);
        contractBean.setContent( IOUtils.toByteArray(multipartFile.getInputStream()));
        contractBean.setStatus(1);
        contractRepository.save(contractBean);
        
        return baseResult;
    }

    @PostMapping("/launchContract")
    @ApiOperation(value = "合同发起", notes = "合同发起")
    public BaseResult launchContract(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("合同文本") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult baseResult = new BaseResult();
        log.info(multipartFile.getOriginalFilename());

        //计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.info("fileHash:" + hash);
        
        ContractInvokeRet ret = cunZhengContract.saveHash2(UserThreadLocal.get().getAccountJson(), password,
                hash, System.currentTimeMillis(),1,0);
        List<Object> list = ret.getReturnList();
        if (list != null && list.size() > 1) {
            ContractBean cb = new ContractBean(hash,new Date(),"fangdong","fangke",
                    hash, multipartFile.getBytes(),0);
            cb.setContractId((new Integer((String) list.get(1))));
            contractRepository.save(cb);
        }
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }

    @PostMapping("/getHash")
    @ApiOperation(value = "原件验证", notes = "文件存证")
    public BaseResult getHash(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult baseResult = new BaseResult();
        log.error(multipartFile.getOriginalFilename());

        //md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.error("fileHash:" + hash);

        ContractInvokeRet ret = cunZhengContract.getFileByHash(UserThreadLocal.get().getAccountJson(), password,
                hash);
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }


    //文件哈希验证 TODO

    @PostMapping("/getFileHash")
    @ApiOperation(value = "文件哈希验证", notes = "文件哈希验证")
    public BaseResult getFileHash(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件哈希") @RequestParam String fileHash
    ) throws Exception {

        BaseResult baseResult = new BaseResult();

        ContractInvokeRet ret = cunZhengContract.getFileByHash(UserThreadLocal.get().getAccountJson(), password,
                fileHash);
        baseResult.returnWithValue(Code.SUCCESS, ret);
        return baseResult;
    }

    //合同状态查询
    @PostMapping("/getContract")
    @ApiOperation(value = "查询合同", notes = "查询合同")
    public BaseResult getContract(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("合同编号") @RequestParam int contractId
    ) throws Exception {

        BaseResult baseResult = new BaseResult();

        ContractBean contractBean = contractRepository.findByContractId(contractId);

        if (contractBean != null && contractBean.getFileHash() != null) {
            ContractInvokeRet ret = cunZhengContract.getFileByHash(UserThreadLocal.get().getAccountJson(), password,
                    contractBean.getFileHash());
            baseResult.returnWithValue(Code.SUCCESS, ret);
        } else {
            baseResult.returnWithValue(Code.CONTRACT_NOT_EXIST, null);
        }

        return baseResult;
    }

    @RequestMapping(path="/downloadContract", method= RequestMethod.GET)
    @ApiOperation(value = "下载合同", notes = "下载合同")
    public ResponseEntity<Resource> downloadContract(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("合同编号") @RequestParam int contractId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control","no-cache,no-store,must-revalidate");
        headers.add("Pragma","no-cache");
        headers.add("Expires","0");
        ContractBean contractBean = contractRepository.findByContractId(contractId);
        if (contractBean == null || contractBean.getContent() == null) {
            String message = "没有合同编号为" + contractId + "的合同";
            return ResponseEntity.ok()
                    .contentLength(message.getBytes().length)
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(new ByteArrayResource(message.getBytes()));

        }
        ByteArrayResource resource = new ByteArrayResource(contractBean.getContent());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(contractBean.getContent().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }

}

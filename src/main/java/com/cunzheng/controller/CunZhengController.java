package com.cunzheng.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cunzheng.Interceptor.UserEntitlement;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.configuration.response.Code;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractInvokeRet;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserRole;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.util.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 电子存证合同管理模块，包括合同发起、合同签署、
 * 合同查询、合同验证、合同下载
 */
@RestController
@RequestMapping("v1/evidence")
@Slf4j
@Api(value = "电子存证", description = "电子存证合同管理模块功能")
public class CunZhengController {

    @Autowired
    private CunZhengContract cunZhengContract;
    @Autowired
    private ContractRepository contractRepository;


    @PostMapping("/launchContract")
    @UserEntitlement(value={UserRole.LANDLORD})
    @ApiOperation(value = "合同发起", notes = "合同发起")
    public BaseResult<ContractInvokeRet> saveEvidence(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("文件") @RequestParam MultipartFile multipartFile
    ) throws Exception {

        BaseResult<ContractInvokeRet> baseResult = new BaseResult<ContractInvokeRet>();
        log.info(multipartFile.getOriginalFilename());

        //md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.info("fileHash:" + hash);


        long currentTimeMillis = System.currentTimeMillis();
        ContractInvokeRet ret = cunZhengContract.saveHash2(UserThreadLocal.get().getAccountJson(), password,
                hash, currentTimeMillis, 1, 0);

        handlerReturnStatus(baseResult, ret);

        if (baseResult.getCode() == 0) {
            ContractBean contractBean = new ContractBean();
            contractBean.setContractId(Integer.parseInt(ret.getReturnList().get(1).toString()));
            contractBean.setContractHash(hash);
            contractBean.setUploadTime(new Date());
            contractBean.setLandlordSignature(null);
            contractBean.setTenantSignature(null);
            contractBean.setFileHash(hash);
            contractBean.setContent(IOUtils.toByteArray(multipartFile.getInputStream()));
            contractBean.setStatus(1);
            contractRepository.save(contractBean);
        }
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

    @RequestMapping(path = "/downloadContract", method = RequestMethod.GET)
    @ApiOperation(value = "下载合同", notes = "下载合同")
    public ResponseEntity<Resource> downloadContract(
            @ApiParam("用户名") @RequestParam String username,
            @ApiParam("密码") @RequestParam String password,
            @ApiParam("合同编号") @RequestParam int contractId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
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

    @PostMapping("/landlordSign")
    @UserEntitlement(value={UserRole.LANDLORD})
    @ApiOperation(value = "房东签约", notes = "房东签约")
    public BaseResult<ContractInvokeRet> landlordSign(@ApiParam("用户") @RequestParam String username,
                                                      @ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
                                                      @ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
                                                      @ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
        log.info("验证用户");

        log.info("开始验证合同是否被更改，状态，以及更新合同");
        BaseResult<ContractInvokeRet> baseResult = new BaseResult<ContractInvokeRet>();
        log.info(multipartFile.getOriginalFilename());

        // md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.info("fileHash:" + hash);

        long currentTimeMillis = System.currentTimeMillis();
        log.info("currentTimeMillis:" + currentTimeMillis);
        String accountJson = UserThreadLocal.get().getAccountJson();
        log.info(accountJson);
        int expectedStatus = 1;
        ContractInvokeRet ret = cunZhengContract.updateFile(accountJson, password, contractId, contractHash, hash,
                currentTimeMillis, expectedStatus);

        handlerReturnStatus(baseResult, ret);
        updateContractIntoDatabase(UserRole.LANDLORD, contractId, multipartFile, baseResult, hash, currentTimeMillis);
        return baseResult;
    }

    @PostMapping("/tenantSign")
    @UserEntitlement(value={UserRole.TENANT})
    @ApiOperation(value = "租客签约", notes = "租客签约")
    public BaseResult<ContractInvokeRet> tenantSign(@ApiParam("用户") @RequestParam String username,
                                                    @ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
                                                    @ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
                                                    @ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
        log.info("验证用户");

        log.info("开始验证合同是否被更改，状态，以及更新合同");
        BaseResult<ContractInvokeRet> baseResult = new BaseResult<ContractInvokeRet>();
        log.info(multipartFile.getOriginalFilename());

        // md5计算哈希
        String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
        log.info("fileHash:" + hash);

        long currentTimeMillis = System.currentTimeMillis();
        log.info("currentTimeMillis:" + currentTimeMillis);
        int expectedStatus = 2;
        ContractInvokeRet ret = cunZhengContract.updateFile(UserThreadLocal.get().getAccountJson(), password,
                contractId, contractHash, hash, currentTimeMillis, expectedStatus);

        handlerReturnStatus(baseResult, ret);
        updateContractIntoDatabase(UserRole.TENANT, contractId, multipartFile, baseResult, hash, currentTimeMillis);
        return baseResult;
    }

    private void updateContractIntoDatabase(UserRole role, int contractId, MultipartFile multipartFile,
                                            BaseResult<ContractInvokeRet> baseResult, String hash, long currentTimeMillis) throws IOException {

        if (baseResult.getCode() == 0) {
            String address = JSONObject.fromObject(UserThreadLocal.get().getAccountJson()).getString("address");

            ContractBean contractBean = contractRepository.findByContractId(contractId);
            contractBean.setContractId(contractId);
            contractBean.setContractHash(hash);
            contractBean.setUploadTime(new Date());
            if ((UserRole.LANDLORD).equals(role)) {
                contractBean.setLandlordSignature(address);
            } else {
                contractBean.setTenantSignature(address);
            }
            contractBean.setFileHash(hash);
            contractBean.setContent(IOUtils.toByteArray(multipartFile.getInputStream()));
            contractBean.setStatus(Integer.parseInt(baseResult.getData().getReturnList().get(1).toString()));
            contractRepository.save(contractBean);
        }
    }

    private void handlerReturnStatus(BaseResult<ContractInvokeRet> baseResult, ContractInvokeRet ret) {
        int status = -1;
        if (ret.getReturnList() != null) {
            status = Integer.parseInt((String) ret.getReturnList().get(0));
        }

        switch (status) {
            case 0:
                baseResult.returnWithValue(Code.SUCCESS, ret);
                break;
            case 2:
                baseResult.returnWithValue(Code.CODE_PEMISSION_DENY, ret);
                break;
            case 3:
                baseResult.returnWithValue(Code.CONTRACT_ALREADY_EXISTED, ret);
                break;
            case 4:
                baseResult.returnWithValue(Code.CODE_FILE_NOT_EXITED, ret);
                break;
            case 5:
                baseResult.returnWithValue(Code.CODE_FILE_MODIFIED, ret);
                break;
            case 6:
                baseResult.returnWithValue(Code.CODE_FILE_STATUS_ERROR, ret);
                break;
            default:
                baseResult.returnWithValue(Code.SYSTEM_ERROR, ret);
                break;
        }

    }
}

package com.cunzheng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.configuration.response.Code;
import com.cunzheng.contract.CunZhengContract;
import com.cunzheng.contract.response.ContractInvokeRet;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.util.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("v1/contract")
@Slf4j
@Api(tags = { "合约签署" })
public class SignContractController {

	@Autowired
	private CunZhengContract cunZhengContract;

	@PostMapping("/landlordSign")
	@ApiOperation(value = "房东签约", notes = "房东签约")
	public BaseResult landlordSign(@ApiParam("用户") @RequestParam String userName,
			@ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
			@ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
			@ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
		log.info("验证用户");

		log.info("开始验证合同是否被更改，状态，以及更新合同");
		BaseResult baseResult = new BaseResult();
		System.out.println(multipartFile.getOriginalFilename());

		// md5计算哈希
		String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
		System.out.println("fileHash:" + hash);

		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("currentTimeMillis:" + currentTimeMillis);
		ContractInvokeRet ret = cunZhengContract.updateFile(UserThreadLocal.get().getAccountJson(), password,
				contractId, contractHash, hash, currentTimeMillis, 1);
		baseResult.returnWithValue(Code.SUCCESS, ret);
		return baseResult;
	}

	@PostMapping("/tenantSign")
	@ApiOperation(value = "租客签约", notes = "租客签约")
	public BaseResult tenantSign(@ApiParam("用户") @RequestParam String userName,
			@ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
			@ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
			@ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
		log.info("验证用户");

		log.info("开始验证合同是否被更改，状态，以及更新合同");
		BaseResult baseResult = new BaseResult();
		System.out.println(multipartFile.getOriginalFilename());

		// md5计算哈希
		String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
		System.out.println("fileHash:" + hash);

		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("currentTimeMillis:" + currentTimeMillis);
		ContractInvokeRet ret = cunZhengContract.updateFile(UserThreadLocal.get().getAccountJson(), password,
				contractId, contractHash, hash, currentTimeMillis, 2);
		baseResult.returnWithValue(Code.SUCCESS, ret);
		return baseResult;
	}
}

package com.cunzheng.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.utils.IOUtils;
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

@RestController
@RequestMapping("v1/contract")
@Slf4j
@Api(tags = { "合约签署" })
public class SignContractController {

	@Autowired
	private CunZhengContract cunZhengContract;

	@Autowired
	private ContractRepository contractRepository;

	@PostMapping("/landlordSign")
	@ApiOperation(value = "房东签约", notes = "房东签约")
	public BaseResult<ContractInvokeRet> landlordSign(@ApiParam("用户") @RequestParam String username,
			@ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
			@ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
			@ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
		log.info("验证用户");

		log.info("开始验证合同是否被更改，状态，以及更新合同");
		BaseResult<ContractInvokeRet> baseResult = new BaseResult<ContractInvokeRet>();
		System.out.println(multipartFile.getOriginalFilename());

		// md5计算哈希
		String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
		System.out.println("fileHash:" + hash);

		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("currentTimeMillis:" + currentTimeMillis);
		String accountJson = UserThreadLocal.get().getAccountJson();
		log.info(accountJson);
		int expectedStatus = 1;
		ContractInvokeRet ret = cunZhengContract.updateFile(accountJson, password, contractId, contractHash, hash,
				currentTimeMillis, expectedStatus);

		handlerReturnStatus(baseResult, ret);
		updateContractIntoDatabase(UserRole.OWNER, contractId, multipartFile, baseResult, hash, currentTimeMillis);
		return baseResult;
	}

	@PostMapping("/tenantSign")
	@ApiOperation(value = "租客签约", notes = "租客签约")
	public BaseResult<ContractInvokeRet> tenantSign(@ApiParam("用户") @RequestParam String username,
			@ApiParam("密码") @RequestParam String password, @ApiParam("合同号") @RequestParam int contractId,
			@ApiParam("之前合同文件Hash(隐藏域)") @RequestParam String contractHash,
			@ApiParam("文件") @RequestParam MultipartFile multipartFile) throws Exception {
		log.info("验证用户");

		log.info("开始验证合同是否被更改，状态，以及更新合同");
		BaseResult<ContractInvokeRet> baseResult = new BaseResult<ContractInvokeRet>();
		System.out.println(multipartFile.getOriginalFilename());

		// md5计算哈希
		String hash = FileUtil.md5HashCode(multipartFile.getInputStream());
		System.out.println("fileHash:" + hash);

		long currentTimeMillis = System.currentTimeMillis();
		System.out.println("currentTimeMillis:" + currentTimeMillis);
		int expectedStatus = 2;
		ContractInvokeRet ret = cunZhengContract.updateFile(UserThreadLocal.get().getAccountJson(), password,
				contractId, contractHash, hash, currentTimeMillis, expectedStatus);

		handlerReturnStatus(baseResult, ret);
		updateContractIntoDatabase(UserRole.RENTER, contractId, multipartFile, baseResult, hash, currentTimeMillis);
		return baseResult;
	}

	private void updateContractIntoDatabase(UserRole role, int contractId, MultipartFile multipartFile,
			BaseResult<ContractInvokeRet> baseResult, String hash, long currentTimeMillis) throws IOException {

		if (baseResult.getCode() == 0) {
			String address = JSONObject.fromObject(UserThreadLocal.get().getAccountJson()).getString("address");

			contractRepository.findByContractId(contractId);
			ContractBean contractBean = new ContractBean();
			contractBean.setContractId(contractId);
			contractBean.setContractHash(hash);
			contractBean.setUploadTime(new Date());
			if ((UserRole.OWNER).equals(role)) {
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

package com.cunzheng.contract;

import cn.hyperchain.sdk.rpc.HyperchainAPI;
import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import cn.hyperchain.sdk.rpc.base.VMType;
import cn.hyperchain.sdk.rpc.function.FuncParamReal;
import cn.hyperchain.sdk.rpc.function.FunctionEncode;
import cn.hyperchain.sdk.rpc.returns.ReceiptReturn;
import cn.hyperchain.sdk.rpc.returns.SingleValueReturn;
import com.cunzheng.configuration.exception.ContractException;
import com.cunzheng.contract.response.Code;
import com.cunzheng.contract.response.ContractDeployRet;
import com.cunzheng.contract.response.ContractInvokeRet;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangrui on 2019/7/3.
 * com.cunzheng.contract
 */
@Slf4j
@Component
public class CunZhengContract {


    /**
     * 部署合约通用接口
     *
     * @param accountJson
     * @param password
     * @return
     * @throws Exception
     */
    public ContractDeployRet deployContract(String accountJson, String password) throws Exception {
        //获取区块链API
        HyperchainAPI hyperchainAPI = BlockUtil.getHyperchainAPI();

        String address = JSONObject.fromObject(accountJson).getString("address");
        String bin = BlockUtil.getBinStr();

        //构建交易
        Transaction transaction = new Transaction(address, bin, false, VMType.EVM);
        //签名
        transaction.signWithSM2(accountJson, password);
        //部署合约
        ReceiptReturn receiptReturn = hyperchainAPI.deployContract(transaction);
        if (!receiptReturn.isSuccess()) {
            log.error("合约部署失败");
            return null;
        }
        String contractAddress = receiptReturn.getContractAddress();
        log.info("contractAddress=" + contractAddress);

        String txHash = receiptReturn.getTxHash();
        SingleValueReturn txReturn = hyperchainAPI.getTxByHash(txHash);
        while (!txReturn.isSuccess()) {
            Thread.sleep(100);
            txReturn = hyperchainAPI.getTxByHash(txHash);
        }
        JSONObject jsonObject = JSONObject.fromObject(txReturn.getResult());

        String blockHash = jsonObject.getString("blockHash");
        String hexBlockNumber = jsonObject.getString("blockNumber");
        String timestamp = jsonObject.getString("timestamp");
        String blockNumber = new BigInteger(hexBlockNumber.substring(2, hexBlockNumber.length()), 16).toString();


        return new ContractDeployRet(contractAddress, txHash, blockHash, blockNumber, timestamp);
    }


    /**
     * 调用合约通用接口
     *
     * @param accountJson
     * @param password
     * @param contractAddress
     * @param funcName
     * @param funcParamReals
     * @param simulate
     * @return
     * @throws Exception
     */
    public ContractInvokeRet callFunction(String accountJson, String password,
                                          String contractAddress, String funcName, FuncParamReal[] funcParamReals, boolean simulate) throws Exception {

        HyperchainAPI blockchainAPI = BlockUtil.getHyperchainAPI();
        String address = JSONObject.fromObject(accountJson).getString("address");
        String abi = BlockUtil.getAbiStr();

        Long startTime = System.currentTimeMillis();
        log.info("开始调用合约，方法名" + funcName + "\n 开始时间：" + startTime);
//        构建交易
        Transaction transaction = new Transaction(address, contractAddress,
                FunctionEncode.encodeFunction(funcName, funcParamReals), simulate, VMType.EVM);
//        签名
        transaction.signWithSM2(accountJson, password);
//        调用
        ReceiptReturn receiptReturn = blockchainAPI.invokeContract(transaction, funcName, abi);

        if (!receiptReturn.isSuccess()) {
            log.error("合约调用失败" + receiptReturn);
            throw new ContractException(Code.CONTRACT_ERROR);
        }

        List<Object> returnList = new ArrayList<>();

        receiptReturn.getDecodedRet().forEach(o -> {
            JSONObject jsonObject = (JSONObject) o;
            returnList.add(jsonObject.getString("mayvalue"));
        });
        log.info("合约调用状态 code :" + receiptReturn.getRawcode());
        log.info("交易哈希 : " + receiptReturn.getTxHash());
        log.info("调用结果 : " + receiptReturn.getRet());
        Long endTime = System.currentTimeMillis();
        log.info("合约调用成功,结束时间" + endTime + "\n 耗时:" + (endTime - startTime) + "ms");

        String txHash = receiptReturn.getTxHash();
        SingleValueReturn txReturn = blockchainAPI.getTxByHash(txHash);
        while (!txReturn.isSuccess()) {
            Thread.sleep(100);
            txReturn = blockchainAPI.getTxByHash(txHash);
        }
        JSONObject jsonObject = JSONObject.fromObject(txReturn.getResult());
        String blockHash = jsonObject.getString("blockHash");
        String hexBlockNumber = jsonObject.getString("blockNumber");
        String timestamp = jsonObject.getString("timestamp");
        String blockNumber = new BigInteger(hexBlockNumber.substring(2, hexBlockNumber.length()), 16).toString();
        return new ContractInvokeRet(returnList, txHash, blockHash, blockNumber, timestamp);
    }


    public ContractInvokeRet saveUser(String userAddress, String userName, int role) throws Exception {

        //获取admin账户
        String json = BlockUtil.getAdminKey();
        JSONObject jsonObject = JSONObject.fromObject(json);
        String accountJson = jsonObject.getString("accountJson");
        String password = jsonObject.getString("password");

        String funcName = "saveUser";
        //数据库
        String contractAddress = BlockUtil.CONTRACT_ADDRESS;
        FuncParamReal[] funcParamReals = new FuncParamReal[3];
        funcParamReals[0] = new FuncParamReal("address", userAddress);
        funcParamReals[1] = new FuncParamReal("bytes32", userName);
        funcParamReals[2] = new FuncParamReal("uint", role);

        return callFunction(accountJson, password, contractAddress,
                funcName, funcParamReals, false);
    }


    public ContractInvokeRet saveHash(String accountJson, String password,
                                      String fileHash, long uploadTime) throws Exception {

        String funcName = "saveHash";
        String contractAddress = BlockUtil.CONTRACT_ADDRESS;

        FuncParamReal[] funcParamReals = new FuncParamReal[2];

        funcParamReals[0] = new FuncParamReal("bytes", fileHash.getBytes(Charset.forName("UTF-8")));
        funcParamReals[1] = new FuncParamReal("uint", uploadTime);

        return callFunction(accountJson, password, contractAddress,
                funcName, funcParamReals, false);
    }


    public ContractInvokeRet getFileByHash(String accountJson, String password,
                                           String fileHash) throws Exception {

        String funcName = "getFileByHash";
        String contractAddress = BlockUtil.CONTRACT_ADDRESS;

        FuncParamReal[] funcParamReals = new FuncParamReal[1];
        funcParamReals[0] = new FuncParamReal("bytes", fileHash.getBytes(Charset.forName("UTF-8")));
        return callFunction(accountJson, password, contractAddress,
                funcName, funcParamReals, false);
    }


}

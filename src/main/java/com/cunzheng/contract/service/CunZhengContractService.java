package com.cunzheng.contract.service;

import cn.hyperchain.sdk.rpc.function.FuncParamReal;
import com.cunzheng.contract.response.ContractDeployRet;
import com.cunzheng.contract.response.ContractInvokeRet;

/**
 * Created by linbo on 2019/7/6.
 */
public interface CunZhengContractService {

    ContractDeployRet deployContract(String accountJson, String password) throws Exception;

    ContractInvokeRet callFunction(String accountJson, String password,
                                   String contractAddress, String funcName, FuncParamReal[] funcParamReals, boolean simulate) throws Exception;
}

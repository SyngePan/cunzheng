package com.cunzheng.configuration.exception;


import com.cunzheng.contract.response.Code;

/**
 * Created by zhangrui on 2019/6/12.
 * com.blockchain.business.exception
 */
public class ContractException extends Exception {

    public ContractException() {

    }

    public ContractException(Code code) {
        super(code.getMessage());
    }

}


package com.cunzheng_01.configuration.exception;


import com.cunzheng_01.contract.response.Code;

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


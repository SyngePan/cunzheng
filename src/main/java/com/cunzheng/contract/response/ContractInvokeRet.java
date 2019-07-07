package com.cunzheng.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zhangrui on 2019/6/17.
 * com.blockchain.contract
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractInvokeRet {

    private List<Object> returnList;
    private String txHash;
    private String blockHash;
    private String blockNum;
    private String opTime;
}

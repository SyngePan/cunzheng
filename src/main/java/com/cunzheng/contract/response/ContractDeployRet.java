package com.cunzheng.contract.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhangrui on 2019/6/17.
 * com.blockchain.contract.response
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractDeployRet {
    private String contractAddress;
    private String txHash;
    private String blockHash;
    private String blockNum;
    private String opTime;

}

package com.cunzheng.controller;

import cn.hyperchain.sdk.rpc.HyperchainAPI;
import cn.hyperchain.sdk.rpc.returns.BlockReturn;
import cn.hyperchain.sdk.rpc.returns.SingleValueReturn;
import com.cunzheng.util.BlockUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by linbo on 2019/7/6.
 */
@RestController
@RequestMapping("v1/test")
@Slf4j
@Api(value = "区块链平台接口测试", description = "区块链平台接口测试")
public class BlockchainController {

    @GetMapping("/getBlkInfoByBlkNum")
    @ApiOperation(value = "根据区块号查询区块信息", notes = "根据区块号查询区块信息")
    public String getBlkInfoByBlkNum(String blockNum) throws Exception {
        HyperchainAPI blockchainAPI = BlockUtil.getHyperchainAPI();
        BlockReturn blockReturn = blockchainAPI.getBlkByNum(blockNum, false);
        return blockReturn.toString();
    }

    @GetMapping("/getBlkInfoByBlkHash")
    @ApiOperation(value = "根据区块哈希查询区块信息", notes = "根据区块哈希查询区块信息")
    public String getBlkInfoByBlkHash(String blockHash) throws Exception {
        HyperchainAPI blockchainAPI = BlockUtil.getHyperchainAPI();
        BlockReturn blockReturn = blockchainAPI.getBlkByHash(blockHash, false);
        return blockReturn.toString();
    }


    @GetMapping("/getTxInfoByTxHash")
    @ApiOperation(value = "根据交易哈希查询交易信息", notes = "根据交易哈希查询交易信息")
    public String getTxInfoByTxHash(String txHash) throws Exception {
        HyperchainAPI blockchainAPI = BlockUtil.getHyperchainAPI();
        SingleValueReturn singleValueReturn =  blockchainAPI.getTxByHash(txHash);
        return singleValueReturn.toString();
    }
}

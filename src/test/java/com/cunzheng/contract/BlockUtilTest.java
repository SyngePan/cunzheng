package com.cunzheng.contract;

import cn.hyperchain.sdk.rpc.HyperchainAPI;
import com.cunzheng.util.BlockUtil;
import net.sf.json.JSONObject;
import org.junit.Test;

/**
 * Created by zhangrui on 2019/7/5.
 * com.example.cunzheng01.contract
 */
public class BlockUtilTest {


    @Test
    public void newAccountSM2() throws Exception {

        String accountJson = BlockUtil.newAccountSM2("123");

        System.out.println(accountJson);

        System.out.println(HyperchainAPI.newAccountRawSM2());
    }

    @Test
    public void deploy() throws Exception {

        BlockUtil.deploy();
    }

    @Test
    public void compile() throws Exception {

        BlockUtil.compile("contractCode/CunZheng.sol");
    }


    @Test
    public void saveUser() throws Exception {

        String accountJson = BlockUtil.newAccountSM2("123");
        String address = JSONObject.fromObject(accountJson).getString("address");
        System.out.println("调用者：" + accountJson);

        BlockUtil.saveUser(address, "Jack", 1);

        BlockUtil.saveUser(address, "Jack", 1);
    }
}
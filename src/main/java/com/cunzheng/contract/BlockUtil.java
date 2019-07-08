package com.cunzheng_01.contract;

import cn.hyperchain.sdk.exception.PwdException;
import cn.hyperchain.sdk.rpc.HyperchainAPI;
import cn.hyperchain.sdk.rpc.Transaction.Transaction;
import cn.hyperchain.sdk.rpc.base.VMType;
import cn.hyperchain.sdk.rpc.function.FuncParamReal;
import cn.hyperchain.sdk.rpc.function.FunctionEncode;
import cn.hyperchain.sdk.rpc.returns.CompileReturn;
import cn.hyperchain.sdk.rpc.returns.ReceiptReturn;
import cn.hyperchain.sdk.rpc.utils.Utils;
import com.cunzheng_01.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by zhangrui on 2019/7/5.
 * com.example.cunzheng01.contract
 */
@Slf4j
public class BlockUtil {


    private final static String CONTRACT_INFO_PATH = "./contract.json";
    private final static String contactName = "CunZheng";

    //缓存合约地址
    public static String CONTRACT_ADDRESS = "";

    public static void compile(String sourceCodePath) throws Exception {

        String srcCode = Utils.readFile(sourceCodePath);

        HyperchainAPI hyperchainAPI = new HyperchainAPI();

        CompileReturn compileReturn = hyperchainAPI.compileContract(srcCode);

        String bin = compileReturn.getBin().get(0);
        String abi = compileReturn.getAbi().get(0);

        //todo 写入文件
        FileUtil.writeBin(bin, contactName);
        FileUtil.writeAbi(abi, contactName);

        //日志打印
        compileReturn.getAbi().forEach(log::info);
        compileReturn.getBin().forEach(log::info);
    }


    public static String newAccountSM2(String pwd) throws GeneralSecurityException, PwdException {

        return HyperchainAPI.newAccountSM2(pwd);

    }


    /**
     * 获取合约abi
     *
     * @return
     * @throws IOException
     */
    public static String getAbiStr() throws IOException {

        return FileUtil.getStringByPath("contractAbi/" + contactName);
    }

    /**
     * 获取合约bin
     *
     * @return
     * @throws IOException
     */
    public static String getBinStr() throws IOException {

        return FileUtil.getStringByPath("contractBin/" + contactName);
    }


    public static HyperchainAPI getHyperchainAPI() throws Exception {
        return new HyperchainAPI();
    }


    public static void writeAdminKey(String json) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(CONTRACT_INFO_PATH);
            fw.write(json);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAdminKey() {

        FileReader fr1 = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fr1 = new FileReader(CONTRACT_INFO_PATH);
            char[] buf = new char[1024];
            int num = 0;
            while ((num = fr1.read(buf)) != -1) {
                stringBuilder.append(new String(buf, 0, num));
            }
        } catch (Exception e) {
            try {
                fr1.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }


    //----------------测试方法-------------//


    private final static String contracAddress = "0x8e80d975824cc29e1b398fae24419daeaedaa45c";

    private final static String accountJson = "{\"address\":\"3617EE6FCD75889BACBCE1C07493CE4C8F57697D\",\"publicKey\":\"04CA6EC31094FF58B13B08F8A32579F76F3C869FC2926DA5B43D7DB1657580C359BE2B2939CDAF4638682F28E14BD076BFD47A70B86D34094250644CFFFC53B439\",\"privateKey\":\"7EF600FEA7B55ABBC91ED7227A2884E77D9035D20C2BD4F0B46340302A519F84C6F431EA0E0E22AC\",\"privateKeyEncrypted\":true,\"version\":\"3.0\",\"algo\":\"0x12\"}";


    //todo 该处用于测试 部署方法另写
    public static void deploy() throws Exception {
        String abi = getAbiStr();
        String bin = getBinStr();

        //获取区块链链接
        HyperchainAPI hyperchainAPI = new HyperchainAPI();

        //获取签名账户 公私钥
        String accountJson = newAccountSM2("123");

        System.out.println("部署私钥：" + accountJson);

        //构建部署交易
        Transaction transaction = new Transaction(
                JSONObject.fromObject(accountJson).getString("address"),
                bin, false, VMType.EVM);

        //交易签名
        transaction.signWithSM2(accountJson, "123");
        //部署

        ReceiptReturn receiptReturn = hyperchainAPI.deployContract(transaction);

        //合约地址
        if (!receiptReturn.isSuccess()) {
            log.info("部署失败");
        }

        log.info("合约地址:" + receiptReturn.getContractAddress());

    }

    //todo 该处用于测试 方法另写
    public static void saveUser(String userAddress, String userName, int role) throws Exception {

        HyperchainAPI hyperchainAPI = new HyperchainAPI();

        String accountJ = accountJson;

        String abi = getAbiStr();

        //
        FuncParamReal[] funcParamReals = new FuncParamReal[3];
        funcParamReals[0] = new FuncParamReal("address", userAddress);
        funcParamReals[1] = new FuncParamReal("bytes32", userName);
        funcParamReals[2] = new FuncParamReal("uint", role);

        Transaction transaction = new Transaction(
                JSONObject.fromObject(accountJ).getString("address"),
                contracAddress, FunctionEncode.encodeFunction(
                "saveUser", funcParamReals)
                , false, VMType.EVM);

        transaction.signWithSM2(accountJ, "123");

        ReceiptReturn receiptReturn = hyperchainAPI.invokeContract(transaction, "saveUser", abi);

        System.out.println(JSONObject.fromObject(receiptReturn).toString(4));
    }


}

package com.cunzheng.contract;

import cn.hyperchain.sdk.crypto.HashUtil;
import cn.hyperchain.sdk.rpc.HyperchainAPI;
import cn.hyperchain.sdk.rpc.function.FunctionDecode;
import cn.hyperchain.sdk.rpc.returns.BlockReturn;
import cn.hyperchain.sdk.rpc.returns.ReceiptReturn;
import cn.hyperchain.sdk.sm.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

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

    //    @Test
//    public void testBlock() throws Exception {
    public static void main(String[] args) throws Exception {


        HyperchainAPI hyperchainAPI = BlockUtil.getHyperchainAPI();

        ArrayList<BlockReturn> blocks = hyperchainAPI.getBlocks(new BigInteger("208"), new BigInteger("209"), false);

        blocks.forEach((BlockReturn blockReturn) -> {

            String txJson = blockReturn.getTransactions();

            JSONArray jsonArray = JSONArray.fromObject(txJson);

            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

            String payload = jsonObject.getString("payload");

            System.out.println("payload:" + payload);

            try {
                Map<String, Object> decodeRet = decodePayload(payload, BlockUtil.getAbiStr());
                System.out.println("decodeRet:" + JSONObject.fromObject(decodeRet).toString(4));
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }





    public static Map<String, Object> decodePayload( String payload, String abi) throws  IOException {

        //参数名与方法参数映射关系
        Map<String, Object> paramsMap = new HashMap<String, Object>();

        //遍历abi，存储methodId的映射关系
        Map<String, com.alibaba.fastjson.JSONObject> methodIdMap = getMethodMap(abi);

        //获取payload对应的方法abi字符串
        com.alibaba.fastjson.JSONObject abiJSON = methodIdMap.get(payload.substring(0, 10));
        if (null == abiJSON) {
            return null;
        }
        //获取该笔交易方法名
        String methodName = abiJSON.containsKey("name") ? abiJSON.getString("name") : "CONTRACT DEPLOY";

        com.alibaba.fastjson.JSONArray inputs = abiJSON.getJSONArray("inputs");
        // 方法有参数
        if (inputs.size() != 0) {
            JSONArray inputArray = null;
            try {
                inputArray = JSONArray.fromObject(net.sf.json.JSONObject.fromObject(
                        FunctionDecode.inputDecode(methodName, JSONArray.fromObject(abiJSON).toString(), payload.substring(10))
                ).get("decodedInput"));
            } catch (UnsupportedEncodingException e) {
                System.out.println("json解析方法payload失败！");
            }
            for (int j = 0; j < inputArray.size(); j++) {
                net.sf.json.JSONObject resultObject = net.sf.json.JSONObject.fromObject(inputArray.get(j));
                if (resultObject.getString("type").equals("array")) {
                    JSONArray arrayBody = JSONArray.fromObject(resultObject.getString("value"));
                    List<String> listArray = new ArrayList<String>();
                    for (int index = 0; index < arrayBody.size(); index++) {
                        net.sf.json.JSONObject value = arrayBody.getJSONObject(index);
                        listArray.add(value.getString("mayvalue"));
                    }
                    paramsMap.put(net.sf.json.JSONObject.fromObject(inputs.get(j)).get("name").toString(), listArray);
                } else {
                    paramsMap.put(net.sf.json.JSONObject.fromObject(inputs.get(j)).get("name").toString(), resultObject.getString("mayvalue"));
                }
            }
        }
        return paramsMap;
    }

    /******************/

    private static Map<String, com.alibaba.fastjson.JSONObject> getMethodMap(String abiStr) throws IOException {

        Map<String, com.alibaba.fastjson.JSONObject> map = new HashMap<>();
        com.alibaba.fastjson.JSONArray jsonArray = com.alibaba.fastjson.JSONArray.parseArray(abiStr);
        for (Object o : jsonArray) {
            com.alibaba.fastjson.JSONObject method = (com.alibaba.fastjson.JSONObject) o;
            map.put(getMethodID(method), method);
        }
        return map;
    }

    private static String getMethodID(com.alibaba.fastjson.JSONObject jsonObject) {
        String fullName = "";
        fullName += jsonObject.getString("name") + "(";
        com.alibaba.fastjson.JSONArray inputs = jsonObject.getJSONArray("inputs");
        for (int i = 0; i < inputs.size(); i++) {
            com.alibaba.fastjson.JSONObject input = inputs.getJSONObject(i);
            fullName += input.getString("type");
            if (i < inputs.size() - 1) {
                fullName += ",";
            }
        }
        fullName += ")";
        return "0x" + sha3(fullName).substring(0, 8);
    }





    public static String sha3(String str) {
        byte[] b = getByteArray(str);
        byte[] r = HashUtil.sha3(b);
        String sha3Str = Util.byteToHex(r);
        return sha3Str.toLowerCase();
    }

    public static byte[] getByteArray(String s) {
        return s != null ? s.getBytes() : null;
    }

    public static String getHexStringByByteArray(byte[] array) {
        if (array == null) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder(array.length * 2);
            Formatter formatter = new Formatter(stringBuilder);
            byte[] arr$ = array;
            int len$ = array.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                byte tempByte = arr$[i$];
                formatter.format("%02x", new Object[]{Byte.valueOf(tempByte)});
            }

            return stringBuilder.toString();
        }
    }

}
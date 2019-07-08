package com.cunzheng_01.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by zhangrui on 2019/3/20.
 * com.blockchain.util
 */

@Slf4j
public class FileUtil {

    /**
     * 获取指定文件的内容
     *
     * @param fileName 文件名
     * @return
     * @throws IOException
     */
    public static String getStringByPath(String fileName) throws IOException {

        String res;
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (null == in) {
            log.error(fileName + "文件不存在");
            throw new IOException(fileName + "文件不存在");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        try {
            String abi;
            while (null != (abi = br.readLine())) {
                sb.append(abi);
            }
            res = sb.toString().trim();
        } catch (IOException e) {
            throw new IOException("读取" + fileName + "文件失败", e);
        }
        return res.trim();
    }

    /**
     * 把合约bin写入文件中
     *
     * @param bin
     * @param contractName
     * @throws IOException
     */
    public static void writeBin(String bin, String contractName) throws IOException {

        //创建目录与文件
        File binDir = new File(FileUtil.class.getResource("/").getPath() + "contractBin/");
        if (!binDir.exists()) {
            binDir.mkdir();
        }

        FileOutputStream fileOutputStream = null;
        try {
            File binFile = new File(FileUtil.class.getResource("/").getPath() + "contractBin/" + contractName);
            if (!binFile.exists()) {
                binFile.createNewFile();
            }
            fileOutputStream = new FileOutputStream(binFile);
            fileOutputStream.write(bin.getBytes());
        } catch (IOException e) {
            log.error("创建bin文件失败");
            throw new IOException("创建bin文件失败", e);
        } finally {
            fileOutputStream.close();
        }
    }


    /**
     * 把合约abi写到abi文件中
     *
     * @param abi
     * @param contractName
     * @throws IOException
     */
    public static void writeAbi(String abi, String contractName) throws IOException {

        FileOutputStream fileOutputStream = null;
        try {

            //创建目录与文件
            File abiDir = new File(FileUtil.class.getResource("/").getPath() + "contractAbi/");
            if (!abiDir.exists()) {
                abiDir.mkdir();
            }

            File abiFile = new File(FileUtil.class.getResource("/").getPath() + "contractAbi/" + contractName);
            if (!abiFile.exists()) {
                abiFile.createNewFile();
            }

            fileOutputStream = new FileOutputStream(abiFile);
            fileOutputStream.write(abi.getBytes());
        } catch (IOException e) {
            log.error("创建abi文件失败");
            throw new IOException("创建abi文件失败", e);
        } finally {
            fileOutputStream.close();
        }
    }


    /**
     * java获取文件的md5值
     *
     * @param fis 输入流
     * @return
     */
    public static String md5HashCode(InputStream fis) {
        try {
            //拿到一个MD5转换器,如果想使用SHA-1或SHA-256，则传入SHA-1,SHA-256
            MessageDigest md = MessageDigest.getInstance("MD5");

            //分多次将一个文件读入，对于大型文件而言，比较推荐这种方式，占用内存比较少。
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            fis.close();
            //转换并返回包含16个元素字节数组,返回数值范围为-128到127
            byte[] md5Bytes = md.digest();
            BigInteger bigInt = new BigInteger(1, md5Bytes);//1代表绝对值
            return bigInt.toString(16);//转换为16进制
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}

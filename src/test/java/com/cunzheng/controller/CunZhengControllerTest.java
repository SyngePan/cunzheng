package com.cunzheng.controller;

import com.cunzheng.configuration.Status;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.repository.ContractRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

/**
 * CunZhengController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jul 9, 2019</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CunZhengControllerTest {
    @Autowired
    private ContractRepository contractRepository;


    @Before
    public void before() throws Exception {


    }

    @After
    public void after() throws Exception {


    }

    /**
     * Method: saveEvidence(@ApiParam("私钥文件") @RequestParam String accountJson, @ApiParam("密码") @RequestParam String password, @ApiParam("文件") @RequestParam MultipartFile multipartFile)
     */
    @Test
    public void testSaveEvidence() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getHash(@ApiParam("私钥文件") @RequestParam String accountJson, @ApiParam("密码") @RequestParam String password, @ApiParam("文件") @RequestParam MultipartFile multipartFile)
     */
    @Test
    public void testGetHash() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getFileHash(@ApiParam("私钥文件") @RequestParam String accountJson, @ApiParam("密码") @RequestParam String password, @ApiParam("文件哈希") @RequestParam String fileHash)
     */
    @Test
    public void testGetFileHash() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: getContract(@ApiParam("私钥文件") @RequestParam String accountJson, @ApiParam("密码") @RequestParam String password, @ApiParam("合同编号") @RequestParam int contractId)
     */
    @Test
    public void testGetContract() throws Exception {

        File file = new File("/Users/chenghao/Desktop/batulu/blockchain/cunzheng/src/test/房屋租赁合同.pdf");

        ContractBean cb = contractRepository.save(new ContractBean("",new Date(),"fangdong","fangke",
                "", Files.readAllBytes(file.toPath()),Status.NEW.name()));

        ContractBean queryCb = contractRepository.findByContractId(cb.getContractId());

        Assert.assertEquals("fangdong", queryCb.getLandlordSignature());

    }


} 

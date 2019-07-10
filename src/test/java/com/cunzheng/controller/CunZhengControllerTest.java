package com.cunzheng.controller;

import com.cunzheng.configuration.response.BaseResult;
import com.cunzheng.contract.BlockUtil;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserRole;
import com.cunzheng.entity.UserThreadLocal;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.repository.UserRepository;
import com.cunzheng.util.FileUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/**
 * CunZhengController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Jul 10, 2019</pre>
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class CunZhengControllerTest {
    private static final String TENANT_NAME = "tenant";
    private static final String TENANT_PASS = "tenant";
    private static final String LANDLORD_NAME = "landLord";
    private static final String LANDLORD_PASS = "landLord";
    private static File file = new File("../../../房屋租赁合同.pdf");
    private static MultipartFile CONTRACT_FILE = null;

    static {
        try {
            CONTRACT_FILE = new MockMultipartFile("test", Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Autowired
    private CunZhengController cunZhengController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private UserController userController;

    @Before
    public void before() throws Exception {

        userController.saveUser(LANDLORD_NAME, LANDLORD_PASS, UserRole.LANDLORD);
        userController.saveUser(TENANT_NAME, TENANT_PASS, UserRole.TENANT);

        UserThreadLocal.set(userRepository.findByUserName(LANDLORD_NAME));

    }

    @After
    public void after() throws Exception {
        UserBean userBean = userRepository.findByUserName(LANDLORD_NAME);
        if (userBean != null) {
            userRepository.delete(userBean);
        }

        UserBean userBean1 = userRepository.findByUserName(TENANT_NAME);
        if (userBean != null) {
            userRepository.delete(userBean1);
        }

    }


    @Test
    public void testSaveEvidence() throws Exception {
        BaseResult ret = cunZhengController.saveEvidence(LANDLORD_NAME, LANDLORD_PASS, CONTRACT_FILE);
        Assert.assertEquals(200, ret.getCode());
    }

    @Test
    public void testSaveEvidence_Deny() throws Exception {
        UserThreadLocal.set(userRepository.findByUserName(TENANT_NAME));
        BaseResult ret = cunZhengController.saveEvidence(TENANT_NAME, TENANT_PASS, CONTRACT_FILE);
        Assert.assertEquals(700, ret.getCode());
        UserThreadLocal.set(userRepository.findByUserName(LANDLORD_NAME));
    }


    @Test
    public void testGetHash() throws Exception {
        BaseResult ret = cunZhengController.getHash(LANDLORD_NAME, LANDLORD_PASS, CONTRACT_FILE);
        Assert.assertEquals(0, ret.getCode());
    }


    @Test
    public void testGetFileHash() throws Exception {
        String hash = FileUtil.md5HashCode(CONTRACT_FILE.getInputStream());
        BaseResult ret = cunZhengController.getFileHash(LANDLORD_NAME, LANDLORD_PASS, hash);
        Assert.assertEquals(0, ret.getCode());
    }


    @Test
    public void testGetContract() throws Exception {
        BaseResult ret = cunZhengController.getContract(LANDLORD_NAME, LANDLORD_PASS, 1);
        Assert.assertEquals(300, ret.getCode());
    }


    @Test
    public void testDownloadContract() throws Exception {
        cunZhengController.downloadContract(LANDLORD_NAME, LANDLORD_PASS, 1);
    }

    @Test
    public void testLandlordSign() throws Exception {
        String hash = FileUtil.md5HashCode(CONTRACT_FILE.getInputStream());
        BaseResult ret = cunZhengController.landlordSign(LANDLORD_NAME, LANDLORD_PASS, 1, hash, CONTRACT_FILE);
        Assert.assertEquals(600, ret.getCode());
    }

    @Test
    public void testLandlordSign_File_Not_Exist() throws Exception {
        BaseResult ret = cunZhengController.landlordSign(LANDLORD_NAME, LANDLORD_PASS, 1, "", CONTRACT_FILE);
        Assert.assertEquals(400, ret.getCode());
    }


    @Test
    public void testTenantSign_File_Not_Exist() throws Exception {
        UserThreadLocal.set(userRepository.findByUserName(TENANT_NAME));
        BaseResult ret = cunZhengController.tenantSign(TENANT_NAME, TENANT_PASS, 1, "", CONTRACT_FILE);
        Assert.assertEquals(400, ret.getCode());

        UserThreadLocal.set(userRepository.findByUserName(LANDLORD_NAME));
    }


}

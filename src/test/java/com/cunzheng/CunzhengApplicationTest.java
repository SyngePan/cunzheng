package com.cunzheng;

import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserBean;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.io.*;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CunzhengApplicationTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContractRepository contractRepository;


	@Test
	public void contractBeanSaveTest() throws Exception {
		ContractBean bean = new ContractBean();
		bean.setContractId(1);
		bean.setUploadTime(new Date());
		bean.setContent(getBytes(System.getProperty("user.home") + "/Downloads/IMG_1.jpg"));

		contractRepository.save(bean);
		//Assert.assertEquals(1,contractRepository.findAll().size());
	}

	@Test
	public void contractBeanGetTest() throws Exception {

		ContractBean bean = contractRepository.findByContractId(1);
		byte[] content = bean.getContent();
		getFile(content, System.getProperty("user.home") + "/Documents", "1.jpg");

	}

	private byte[] getBytes(String filePath) {
		byte[] buffer = null;
		FileInputStream is = null;
		ByteArrayOutputStream bos = null;
		try {
			File file = new File(filePath);
			is = new FileInputStream(file);
			bos = new ByteArrayOutputStream(1024);
			byte[] b = new byte[1024];
			int n;
			while( (n = is.read(b)) != -1 ) {
				bos.write(b,0,n);
			}
			is.close();
			bos.close();
			buffer = bos.toByteArray();

		} catch (Exception e) {
			log.error(e.toString(),e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					log.error(e.toString(),e);				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e) {
					log.error(e.toString(),e);				}
			}
		}
		return buffer;
	}

	private void getFile(byte[] bFile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {
				dir.mkdir();
			}
			file = new File(filePath + File.separator + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bFile);
		} catch (Exception e) {
			log.error(e.toString(),e);
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch(Exception e) {
					log.error(e.toString(),e);
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					log.error(e.toString(),e);
				}
			}
		}
	}


}


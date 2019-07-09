package com.cunzheng;

import com.cunzheng.configuration.Status;
import com.cunzheng.entity.ContractBean;
import com.cunzheng.entity.UserBean;
import com.cunzheng.repository.ContractRepository;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CunzhengApplicationTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContractRepository contractRepository;


	@Test
	public void contextLoads() throws IOException {


		/*userRepository.save(new UserBean("aaa", "aaa", UserRole.OWNER,null));
		userRepository.save(new UserBean("bbb","bbb",UserRole.OWNER,null));
		userRepository.save(new UserBean("ccc","ccc",UserRole.OWNER,null));
		userRepository.save(new UserBean("ddd","ddd",UserRole.OWNER,null));
		userRepository.save(new UserBean("eee","eee",UserRole.OWNER,null));

		//Assert.assertEquals(9,userRepository.findAll().size());

		Assert.assertEquals(5,userRepository.findByUserName("eee").getUserId());


		userRepository.delete(userRepository.findByUserName("aaa"));*/
		//Assert.assertEquals(4,userRepository.findAll().size());

		File file = new File("/Users/chenghao/Desktop/batulu/blockchain/cunzheng/src/test/房屋租赁合同.pdf");

		contractRepository.save(new ContractBean("",new Date(),"fangdong","fangke",
				"", Files.readAllBytes(file.toPath()), Status.NEW.name()));

		ContractBean cb = contractRepository.findByContractId(1);
		//contractRepository.save(new ContractBean(2,"",new Date(),"fangdong1","fangke1",""));
		//contractRepository.save(new ContractBean(3,"",new Date(),"fangdong2","fangke2",""));
		//contractRepository.save(new ContractBean(4,"",new Date(),"fangdong3","fangke3",""));
		//contractRepository.save(new ContractBean(5,"",new Date(),"fangdong4","fangke4",""));


		//Assert.assertEquals(5,contractRepository.findAll().size());

	}

}

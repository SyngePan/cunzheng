package com.cunzheng;

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

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CunzhengApplicationTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContractRepository contractRepository;


	@Test
	public void contextLoads() {


		/*userRepository.save(new UserBean("aaa", "aaa", UserRole.OWNER,null));
		userRepository.save(new UserBean("bbb","bbb",UserRole.OWNER,null));
		userRepository.save(new UserBean("ccc","ccc",UserRole.OWNER,null));
		userRepository.save(new UserBean("ddd","ddd",UserRole.OWNER,null));
		userRepository.save(new UserBean("eee","eee",UserRole.OWNER,null));

		//Assert.assertEquals(9,userRepository.findAll().size());

		Assert.assertEquals(5,userRepository.findByUserName("eee").getUserId());


		userRepository.delete(userRepository.findByUserName("aaa"));
		//Assert.assertEquals(4,userRepository.findAll().size());

		contractRepository.save(new ContractBean(1,"",new Date(),"fangdong","fangke",""));
		contractRepository.save(new ContractBean(2,"",new Date(),"fangdong1","fangke1",""));
		contractRepository.save(new ContractBean(3,"",new Date(),"fangdong2","fangke2",""));
		contractRepository.save(new ContractBean(4,"",new Date(),"fangdong3","fangke3",""));
		contractRepository.save(new ContractBean(5,"",new Date(),"fangdong4","fangke4",""));


		//Assert.assertEquals(5,contractRepository.findAll().size());


		System.out.println(contractRepository.findByContractId(1));*/

	}

}

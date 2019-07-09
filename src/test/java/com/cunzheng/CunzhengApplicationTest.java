package com.cunzheng;

import com.cunzheng.entity.UserBean;
import com.cunzheng.entity.UserRole;
import com.cunzheng.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CunzhengApplicationTest {

	@Autowired
	private UserRepository userRepository;


	@Test
	public void contextLoads() {


		userRepository.save(new UserBean("aaa", "aaa", UserRole.OWNER,null));
		userRepository.save(new UserBean("bbb","bbb",UserRole.OWNER,null));
		userRepository.save(new UserBean("ccc","ccc",UserRole.OWNER,null));
		userRepository.save(new UserBean("ddd","ddd",UserRole.OWNER,null));
		userRepository.save(new UserBean("eee","eee",UserRole.OWNER,null));

		Assert.assertEquals(5,userRepository.findAll().size());

		//Assert.assertEquals(4,userRepository.findByUserName("eee").getUserId());

		userRepository.delete(userRepository.findByUserName("aaa"));
		Assert.assertEquals(4,userRepository.findAll().size());

	}

}

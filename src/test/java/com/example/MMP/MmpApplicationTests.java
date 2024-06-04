package com.example.MMP;

import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MmpApplicationTests {

	@Autowired
	private SiteUserRepository siteUserRepository;
	@Test
	public void createUSer() {
		SiteUser siteUser = new SiteUser();
		siteUser.setName("이지영");
		siteUser.setGender("여자");
		siteUser.setEmail("dlwl0716@naver.com");
		siteUser.setBirthDate("010716");
		siteUser.setUserId("dlwldud");
		siteUser.setPassword("dlwldud");
		siteUser.setUserRole("user");

		siteUserRepository.save(siteUser);
	}

}

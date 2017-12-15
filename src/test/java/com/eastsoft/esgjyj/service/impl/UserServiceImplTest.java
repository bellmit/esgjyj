package com.eastsoft.esgjyj.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.eastsoft.esgjyj.EsgjyjApplication;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.UserWithBLOBs;
import com.eastsoft.esgjyj.form.TreeNode;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(EsgjyjApplication.class)
public class UserServiceImplTest {
	private static final String COURT_NO = "0FC";
	private static final String USERID = "0FC00001";
	
	@Autowired
	private UserServiceImpl userService;
	
	@Test
	public void testList() {
		List<User> list = this.userService.list(COURT_NO);
		assertTrue(list.size() > 0);
		int index = 0;
		for (User user : list) {
			System.out.println(++index + ":" + user);
		}
	}
	
	@Test
	public void testListEnable() {
		List<User> list = this.userService.list(COURT_NO);
		List<User> listEnable = this.userService.listEnable(COURT_NO);
		assertTrue(list.size() > listEnable.size());
		int index = 0;
		for (User user : listEnable) {
			System.out.println(++index + ":" + user);
		}
	}
	
	@Test
	public void testGet() {
		UserWithBLOBs user = this.userService.get(USERID);
		assertNotNull(user);
		System.out.println(user);
	}

	@Test
	public void testGetProgramTreeNodes() {
		List<TreeNode> rootList = this.userService.getProgramTreeNodes(USERID);
		assertTrue(rootList.size() > 0);
		for (TreeNode node : rootList) {
			System.out.println(node);
		}
	}
}

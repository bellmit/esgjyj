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
import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.form.TreeNode;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(EsgjyjApplication.class)
public class OfficeServiceImplTest {
	private static final String COURT_NO = "0FC";
	private static final String OFID = "0FC00001";
	
	@Autowired
	private OfficeServiceImpl officeService;

	@Test
	public void testList() {
		List<Office> list = this.officeService.list(COURT_NO);
		assertTrue(list.size() > 0);
		int index = 0;
		for (Office office : list) {
			System.out.println(++index + ":" + office);
		}
	}

	@Test
	public void testListEnable() {
		List<Office> list = this.officeService.list(COURT_NO);
		List<Office> listEnable = this.officeService.listEnable(COURT_NO);
		assertTrue(list.size() > listEnable.size());
		int index = 0;
		for (Office office : listEnable) {
			System.out.println(++index + ":" + office);
		}
	}
	
	@Test
	public void testGet() {
		Office office = this.officeService.get(OFID);
		assertNotNull(office);
		System.out.println(office);
	}

	@Test
	public void testGetOfficeUserTreeNodes() {
		List<TreeNode> list = this.officeService.getOfficeUserTreeNodes(COURT_NO);
		assertTrue(list.size() > 0);
		int index = 0;
		for (TreeNode node : list) {
			System.out.println(++index + ":" + node);
		}
	}
}

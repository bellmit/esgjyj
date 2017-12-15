package com.eastsoft.esgjyj.service.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.eastsoft.esgjyj.EsgjyjApplication;
import com.eastsoft.esgjyj.domain.Program;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(EsgjyjApplication.class)
@Transactional
public class ProgramServiceImplTest {
	@Autowired
	private ProgramServiceImpl programService;

	@Test
	public void testListAccessable() {
		List<Program> list = this.programService.listAccessable("0FC00001");
		assertTrue(list.size() > 0);
		int index = 0;
		for (Program program : list) {
			System.out.println(index++ + ":" + program);
		}
	}
}

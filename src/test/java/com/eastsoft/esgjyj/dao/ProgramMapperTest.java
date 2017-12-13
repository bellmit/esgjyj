package com.eastsoft.esgjyj.dao;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ProgramMapperTest {
	@Autowired
	private ProgramMapper programMapper;
	
	@Value("${system.level}")
	private String systemLevel;

	@Test
	public void testSelectByParams() {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", "0FC");
		params.put("ownerId", "0FC00001");
		params.put("parentLevel", systemLevel);
		List<Program> list = this.programMapper.selectByParams(params);
		assertTrue(list.size() > 0);
		int index = 0;
		for (Program program : list) {
			System.out.println(index++ + ":" + program);
		}
	}
}

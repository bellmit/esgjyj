package com.eastsoft.esgjyj.dao;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.eastsoft.esgjyj.EsgjyjApplication;
import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.enums.OfficeState;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(EsgjyjApplication.class)
public class OfficeMapperTest {
	@Autowired
	private OfficeMapper officeMapper;

	@Test
	public void testSelectByParams() {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", "0FC");
		params.put("state", OfficeState.ENABLE.getValue());
		List<Office> list = this.officeMapper.selectByParams(params);
		assertTrue(list.size() > 0);
		int index = 0;
		for (Office office : list) {
			System.out.println(++index + ":" + office);
		}
	}
}

package com.eastsoft.esgjyj.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SjyReportServiceImplTest {
	@Test
	public void testSjy() {
		Map<String, Object> map = new HashMap<>();
		map.put("khid", "d7874cc6e070423a98ac55021b88fc63");
		map.put("ksrq", "2017-01-01");
		map.put("jzrq", "2017-12-18");
		SjyReportServiceImpl sjyReportService = new SjyReportServiceImpl();
		sjyReportService.sjyList(map);
	}
}

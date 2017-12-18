package com.eastsoft.esgjyj.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.eastsoft.esgjyj.service.impl.FgzlReportServiceImpl;
import com.eastsoft.esgjyj.service.impl.GySpyjkhServiceImpl;
import com.eastsoft.esgjyj.service.impl.SjyReportServiceImpl;

@Controller
public class HelloController {
	@Autowired
	GySpyjkhServiceImpl GySpyjkhService;
	@Autowired
	SjyReportServiceImpl sjyReportService;
	@Autowired
	FgzlReportServiceImpl fgzlReportService;
	@GetMapping("/hello")
	public void hello() {
//		GySpyjkhService.execute();
		Map<String, Object> map = new HashMap<>();
		map.put("khid", "d7874cc6e070423a98ac55021b88fc63");
		map.put("ksrq", "2017-01-01");
		map.put("jzrq", "2017-12-18");
		sjyReportService.sjyList(map);
		fgzlReportService.fgzlList(map);
	}
}

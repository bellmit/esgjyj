package com.eastsoft.esgjyj.controller;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.LazyDynaBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.service.impl.FgzlReportServiceImpl;
import com.eastsoft.esgjyj.service.impl.GySpyjkhServiceImpl;
import com.eastsoft.esgjyj.service.impl.RedisServiceImpl;
import com.eastsoft.esgjyj.service.impl.SjyReportServiceImpl;
import com.eastsoft.esgjyj.util.ExcelUtil;

@Controller
public class HelloController {
	@Autowired
	GySpyjkhServiceImpl GySpyjkhService;
	@Autowired
	SjyReportServiceImpl sjyReportService;
	@Autowired
	FgzlReportServiceImpl fgzlReportService;
	@Autowired
	RedisServiceImpl redisService;
	@Autowired
	BaseDao baseDao;
	@GetMapping("/hello")
	public void hello() {
		GySpyjkhService.execute();
//		Map<String, Object> map = new HashMap<>();
//		map.put("khid", "d7874cc6e070423a98ac55021b88fc63");
//		map.put("ksrq", "2017-01-01");
//		map.put("jzrq", "2017-12-18");
//		sjyReportService.sjyList(map);
//		fgzlReportService.fgzlList(map);
	}
	//导出入额法官办案基础原始分
	@GetMapping("/szfgExcel")
	public void refgDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sql = "select * from YJKH where COURT_NO = '0F' and ACTIVE = '1'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		for(Map<String, Object> item : list) {
			LazyDynaBean table = redisService.getTable(item);
			String[][] thead = (String[][])table.get("thead");
			String[][] tbody = (String[][])table.get("tbody");
			String caption = (String)table.get("caption");
			ExcelUtil.export(request, response, caption, caption, thead, tbody);
		}
	}
	//导出法官助理岗位业绩基础分
	@GetMapping("/fgzlExcel")
	public void fgzlDataExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String sql = "select * from YJKH where COURT_NO = '0F' and ACTIVE = '1'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		for(Map<String, Object> item : list) {
			LazyDynaBean table = redisService.getFgzlTable(item);
			String[][] thead = (String[][])table.get("thead");
			String[][] tbody = (String[][])table.get("tbody");
			String caption = (String)table.get("caption");
			ExcelUtil.export(request, response, caption, caption, thead, tbody);
		}
	}
}

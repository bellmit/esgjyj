package com.eastsoft.esgjyj.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eastsoft.esgjyj.service.impl.GySpyjkhServiceImpl;

@Component
public class TestJob {
	@Autowired
	private GySpyjkhServiceImpl gySpyjkhService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Scheduled(cron = "0 0 22 * * ? ")
	public void execute() {
		logger.info("---------------------高院业绩考核开始--------------------");
		try {
			gySpyjkhService.execute();
		} catch (Exception e) {
			throw new RuntimeException("计算绩效考核报错！");
		}
		logger.info("---------------------高院业绩考核结束--------------------");
	}
}

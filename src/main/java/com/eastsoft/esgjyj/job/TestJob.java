package com.eastsoft.esgjyj.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {
	Logger logger = LoggerFactory.getLogger(getClass());
	
//	@Scheduled(cron = "0/5 * * * * ? ")
//	public void execute() {
//		logger.info("I am a job.");
//	}
}

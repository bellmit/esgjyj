package com.eastsoft.esgjyj.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 多线程配置。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean
	@Description("默认多线程执行器")
	public Executor defaultExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
		executor.setCorePoolSize(5);  
		executor.setMaxPoolSize(5);  
		executor.setQueueCapacity(200);
		executor.setThreadNamePrefix("defaultExecutor-");  
		executor.initialize();  
		return executor; 
	}
}

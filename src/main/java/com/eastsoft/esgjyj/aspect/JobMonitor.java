package com.eastsoft.esgjyj.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 作业监控切面。
 * <p>监控 com.eastsoft.esgjyj.job 包及其子包下所有类中的 execute 方法，
 * 在作业开始执行、结束执行时打印状态日志（包含执行时间，单位秒）。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Aspect
@Component
public class JobMonitor {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Around("execution(* com.eastsoft.esgjyj.job..*.execute(..))")
	public Object monitorJob(ProceedingJoinPoint pjp) throws Throwable {
		String targetClass = pjp.getTarget().getClass().toString().substring("class ".length());
		logger.info("{} 作业开始执行...", targetClass);
		long startTime = System.currentTimeMillis();
		
		Object retVal = pjp.proceed();
		
		long endTime = System.currentTimeMillis();
		String consume = String.format("%.2f", 1.0 * (endTime - startTime) / 1000) + "秒。";
		logger.info("{} 作业结束，耗时 {}", targetClass, consume);
		return retVal;
	}
}

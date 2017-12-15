package com.eastsoft.esgjyj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 性能监控拦截器。
 * <p>计算从拦截到请求到响应完成消耗的时间，当消耗时间大于标准时间 {@link #standard} 时以 WARN 级别记录日志。
 * @author chenkai
 * @since 1.0.0
 * @version 1.0.0
 */
@Component
public class MonitorInterceptor extends HandlerInterceptorAdapter {
	Logger logger = LoggerFactory.getLogger(MonitorInterceptor.class);
	
	private NamedThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<>("Monitor-StartTime");
	
	private Long standard;
	
	public MonitorInterceptor() {
		// 响应标准时间，默认1秒。
		this.standard = 1000L;
	}
	
	public MonitorInterceptor(Long standard) {
		this.standard = standard;
	}
	
	public Long getStandard() {
		return standard;
	}

	public void setStandard(Long standard) {
		this.standard = standard;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			long beginTime = System.currentTimeMillis();
	        startTimeThreadLocal.set(beginTime);
		}
		
        return true;
	}
	
	@Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) throws Exception {  
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
	        long beginTime = startTimeThreadLocal.get();
	        long endTime = System.currentTimeMillis();
	        long consume = endTime - beginTime;
	        if	(consume > standard) {
	        		logger.warn("{} consume {}ms", request.getRequestURI(), consume);
	        }
		}
    }
}
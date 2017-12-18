package com.eastsoft.esgjyj.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 应用上下文工具类，用于从 IOC 容器中获取实例化对象。
 * @author chenkai
 * @since archetype-1.0.0
 * @version 1.0.0
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ApplicationContextUtil.applicationContext = applicationContext;
	}
	
	/**
	 * 获取应用上下文。
	 * @return 应用上下文对象。
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取实例化对象。
	 * @param name 对象名。
	 * @return 实例化对象。
	 * @see org.springframework.beans.factory.BeanFactory#getBean(Class)
	 */
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	/**
	 * 获取实例化对象。
	 * @param name 对象名。
	 * @param requiredType 实例化对象类型。
	 * @return 实例化对象。
	 * @see org.springframework.beans.factory.BeanFactory#getBean(String, Class)
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}
}
//package com.eastsoft.esgjyj.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
//import com.eastsoft.esgjyj.interceptor.AuthenticationInterceptor;
//import com.eastsoft.esgjyj.interceptor.MonitorInterceptor;
//import com.eastsoft.esgjyj.interceptor.ScopeInterceptor;
//
///**
// * Spring MVC 配置。
// * @author Ben
// * @since 1.0.0
// * @version 1.0.0
// */
//@Configuration
//public class SpringMvcConfig extends WebMvcConfigurerAdapter {
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new ScopeInterceptor());
//		registry.addInterceptor(new MonitorInterceptor(5000L));
//		registry.addInterceptor(new AuthenticationInterceptor());
//		super.addInterceptors(registry);
//	}
//}

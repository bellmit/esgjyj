/*
package com.eastsoft.esgjyj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.eastsoft.esgjyj.context.ApplicationContextUtil;
import com.eastsoft.esgjyj.context.ScopeUtil;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.UserWithBLOBs;
import com.eastsoft.esgjyj.service.impl.UserServiceImpl;

*/
/**
 * 作用域拦截器。
 * <p>把 {@link HttpServletRequest} 和 {@link HttpServletResponse} 绑定到 {@link ScopeUtil} 。
 * @author mengbin
 * @since 1.0.0
 * @version 1.1.0, 2017/9/15
 *//*

@Component
public class ScopeInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
			// TODO: 测试代码，用于伪登录，生产环境删除！！！
			User sessionUser = (User) request.getSession().getAttribute("user");
			if (sessionUser == null) {
				UserServiceImpl userService = ApplicationContextUtil.getBean("userService", UserServiceImpl.class);
				UserWithBLOBs user = userService.get("0F000001");
				sessionUser = new User();
				BeanUtils.copyProperties(user, sessionUser);
				request.getSession().setAttribute("user", user);
			}
			
			ScopeUtil.setRequest(request);
			ScopeUtil.setResponse(response);
		}
		
        return true;
	}
}*/

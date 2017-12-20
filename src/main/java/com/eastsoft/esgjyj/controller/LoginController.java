package com.eastsoft.esgjyj.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eastsoft.esgjyj.service.impl.UserServiceImpl;
import com.eastsoft.esgjyj.util.R;


/**

 */
@RestController
public class LoginController {
	@Autowired
	private UserServiceImpl userService;
	
    @GetMapping("/")
    void login(HttpServletResponse response){
        try {
            response.sendRedirect("/login.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @GetMapping("/autoLogin")
    public void autoLogin(HttpServletResponse response, String userinfo) {
    	String info = Base64.decodeToString(userinfo);
    	String logid = "";
    	Pattern pattern = Pattern.compile(".*logid=(.*)&");
    	Matcher matcher = pattern.matcher(info);
    	if(matcher.find()) {
    		logid = matcher.group(1);
    	} else {
    		throw new RuntimeException("参数解析错误");
    	}
    	String logpass = userService.getPassword(logid);
    	UsernamePasswordToken token = new UsernamePasswordToken(logid, logpass);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            try {
				response.sendRedirect("/index_v1.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
        } catch (AuthenticationException e) {
        	throw new AuthenticationException();
        }
    }
    
    @PostMapping("/ajaxLogin")
    R ajaxLogin(String username, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            return R.ok();
        } catch (AuthenticationException e) {
            return R.error("用户或密码错误");
        }
    }
}

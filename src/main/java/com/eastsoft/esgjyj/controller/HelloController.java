package com.eastsoft.esgjyj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.eastsoft.esgjyj.service.impl.GySpyjkhServiceImpl;

@Controller
public class HelloController {
	@Autowired
	GySpyjkhServiceImpl GySpyjkhService;
	@GetMapping("/hello")
	public void hello() {
		GySpyjkhService.execute();
	}
}

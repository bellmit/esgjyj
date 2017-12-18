package com.eastsoft.esgjyj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.ProgramMapper;
import com.eastsoft.esgjyj.domain.Program;
import com.eastsoft.esgjyj.domain.UserWithBLOBs;

/**
 * 模块业务逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Service("programService")
public class ProgramServiceImpl {
	@Autowired
	private ProgramMapper prograMpper;
	@Autowired
	private UserServiceImpl userService;
	
	@Value("${system.level}")
	private String systemLevel;
	
	/**
	 * 获取用户拥有权限的模块有序列表。
	 * @param userid 用户标识。
	 * @return 用户拥有权限的模块有序列表。
	 */
	public List<Program> listAccessable(String userid) {
		UserWithBLOBs user = this.userService.get(userid);
		if (user == null) {
			return new ArrayList<Program>();
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", user.getCourtNo());
		params.put("ownerId", user.getUserid());
		params.put("parentLevel", systemLevel);
		List<Program> list = this.prograMpper.selectByParams(params);
		return list;
	}
}

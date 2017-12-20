package com.eastsoft.esgjyj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.eastsoft.esgjyj.domain.Office;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.BaseDao;
import com.eastsoft.esgjyj.dao.UserMapper;
import com.eastsoft.esgjyj.domain.Program;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.domain.UserWithBLOBs;
import com.eastsoft.esgjyj.enums.UserState;
import com.eastsoft.esgjyj.form.TreeNode;
import com.eastsoft.esgjyj.util.PassWd;

/**
 * 用户业务逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Service("userService")
public class UserServiceImpl {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ProgramServiceImpl programService;
	@Autowired
	private BaseDao baseDao;
	
	/**
	 * 获取指定法院所有用户。
	 * @param courtNo 法院编号。
	 * @return 所有用户列表。
	 */
	public List<User> list(String courtNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", courtNo);
		List<User> list = this.userMapper.selectByParams(params);
		return list;
	}
	
	/**
	 * 获取指定法院有效用户。
	 * @param courtNo 法院编号。
	 * @return 所有用户列表。
	 */
	public List<User> listEnable(String courtNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", courtNo);
		params.put("state", UserState.ENABLE.getValue());
		List<User> list = this.userMapper.selectByParams(params);
		return list;
	}
	
	/**
	 * 获取用户。
	 * @param userid 用户标识。
	 * @return 存在时返回用户对象，否则返回 null 。
	 */
	public UserWithBLOBs get(String userid) {
		UserWithBLOBs user = this.userMapper.selectByPrimaryKey(userid);
		return user;
	}
	
	/**
	 * 获取用户模块树。
	 * @param userid 用户表示。
	 * @return 模块树节点列表。
	 */
	public List<TreeNode> getProgramTreeNodes(String userid) {
		List<Program> programList = this.programService.listAccessable(userid);
		Map<String, TreeNode> nodeMap = new LinkedHashMap<>();
		List<TreeNode> nodeList = new ArrayList<>();
		TreeNode node, parentNode;
		String level, parentLevel;
		for (Program program : programList) {
			level = program.getMdlevel();
			if (level.length() < 4) {
				continue;
			}
			
			node = new TreeNode();
			node.setId(program.getMdid());
			node.setText(program.getMdcaption());
			node.setState(TreeNode.OPEN);
			Map<String, Object> attributes = new HashMap<>();
			attributes.put("level", program.getMdlevel());
			attributes.put("link", program.getMdname());
			attributes.put("icon", program.getMdicon());
			node.setAttributes(attributes);
			node.setChildren(new ArrayList<TreeNode>());
			nodeMap.put(level, node);
			
			// 一级目录
			if (level.length() == 4) {
				nodeList.add(node);
			}
			
			// 二级及以下
			parentLevel = program.getMdlevel().substring(0, program.getMdlevel().length() - 2);
			parentNode = nodeMap.get(parentLevel);
			if (parentNode != null) {
				parentNode.getChildren().add(node);
				parentNode.setState(TreeNode.CLOSED);
			}
		}
		return nodeList;
	}
	/**
	 * 获取登录密码
	 * @param logid   登陆名
	 * @return
	 */
	public String getPassword(String logid) {
		String sql = "select LOGPASS from S_USER where LOGID = '" + logid + "'";
		List<Map<String, Object>> list = baseDao.queryForList(sql);
		String logpass = list.get(0).get("LOGPASS") == null ? "" : (String)list.get(0).get("LOGPASS");
		PassWd passWd = new PassWd();
		logpass = passWd.f_decrypt(logpass);
		return logpass;
	}
	public Office getOfficeByuserId(String userId){
		return userMapper.getByUserId( userId);
	}
}

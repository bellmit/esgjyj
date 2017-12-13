package com.eastsoft.esgjyj.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eastsoft.esgjyj.dao.OfficeMapper;
import com.eastsoft.esgjyj.domain.Office;
import com.eastsoft.esgjyj.domain.User;
import com.eastsoft.esgjyj.enums.OfficeState;
import com.eastsoft.esgjyj.form.TreeNode;

/**
 * 部门业务逻辑。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
@Service("officeService")
public class OfficeServiceImpl {
	@Autowired
	private OfficeMapper officeMapper;
	@Autowired
	private UserServiceImpl userService;
	
	/**
	 * 获取指定法院所有部门列表。
	 * @param courtNo 法院编号。
	 * @return 所有部门列表。
	 */
	public List<Office> list(String courtNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", courtNo);
		List<Office> list = this.officeMapper.selectByParams(params);
		return list;
	}
	
	/**
	 * 获取指定法院有效部门列表。
	 * @param courtNo 法院编号。
	 * @return 有效部门列表。
	 */
	public List<Office> listEnable(String courtNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("courtNo", courtNo);
		params.put("state", OfficeState.ENABLE.getValue());
		List<Office> list = this.officeMapper.selectByParams(params);
		return list;
	}
	
	/**
	 * 获取部门。
	 * @param ofid 部门标识。
	 * @return 存在时返回对象，否则返回 null 。
	 */
	public Office get(String ofid) {
		Office office = this.officeMapper.selectByPrimaryKey(ofid);
		return office;
	}
	
	/**
	 * 获取部门人员树节点列表。
	 * @param courtNo 法院编号。
	 * @return 部门人员树节点列表。
	 */
	public List<TreeNode> getOfficeUserTreeNodes(String courtNo) {
		List<Office> officeList = this.listEnable(courtNo);
		List<User> userList = this.userService.listEnable(courtNo);
		
		List<TreeNode> nodes = new ArrayList<>();
		Map<String, TreeNode> nodeMap = new HashMap<>();
		TreeNode node;
		for (Office office : officeList) {
			node = new TreeNode();
			node.setId("off_" + office.getOfid());
			node.setText(office.getShortname());
			//node.setState(TreeNode.OPEN);
			node.setChildren(new ArrayList<TreeNode>());
			node.setAttributes(new HashMap<String, Object>());
			nodes.add(node);
			nodeMap.put(office.getOfid(), node);
		}
		TreeNode parentNode;
		for (User user : userList) {
			node = new TreeNode();
			node.setId("user_" + user.getUserid());
			node.setText(user.getUsername());
		//	node.setState(TreeNode.OPEN);
			node.setChildren(new ArrayList<TreeNode>());
			node.setAttributes(new HashMap<String, Object>());
			
			parentNode = nodeMap.get(user.getOfficeid());
			if (parentNode != null) {
				parentNode.getChildren().add(node);
				parentNode.setState(TreeNode.CLOSED);
			}
		}
		return nodes;
	}

	public List<TreeNode> getOfficeTreeNodes(String courtNo) {
		List<Office> officeList = this.listEnable(courtNo);
		List<User> userList = this.userService.listEnable(courtNo);

		List<TreeNode> nodes = new ArrayList<>();
		Map<String, TreeNode> nodeMap = new HashMap<>();
		TreeNode node;
		for (Office office : officeList) {
			node = new TreeNode();
			node.setId(office.getOfid());
			node.setText(office.getShortname());
			node.setChildren(new ArrayList<TreeNode>());
			node.setAttributes(new HashMap<String, Object>());
			nodes.add(node);
			nodeMap.put(office.getOfid(), node);
		}

		return nodes;
	}
}

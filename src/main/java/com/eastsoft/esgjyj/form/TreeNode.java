package com.eastsoft.esgjyj.form;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Easyui 树节点。
 * @author mengbin
 * @since 2014/10/28
 * @version 1.0.1, 2017/8/8
 */
public class TreeNode implements Serializable {
	/**
	 * 状态_展开
	 */
	public static final String OPEN = "open";
	/**
	 * 状态_闭合
	 */
	public static final String CLOSED = "closed";
	
	private static final long serialVersionUID = -4483845131804232463L;
	
	private String id;
	
	private String text;
	
	private String state;
	
	private Map<String, Object> attributes;
	
	private List<TreeNode> children;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "TreeNode [id=" + id + ", text=" + text + ", state=" + state + ", attributes=" + attributes
				+ ", children=" + children + "]";
	}
}

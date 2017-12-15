package com.eastsoft.esgjyj.enums;

/**
 * 枚举部门状态。
 * @author Ben
 * @since 1.0.0
 * @version 1.0.0
 */
public enum OfficeState {
	ENABLE("0", "启用"),
	DISABLE("1", "停用");
	
	private final String value;
	
	private final String text;
	
	private OfficeState(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public String getText() {
		return text;
	}
}

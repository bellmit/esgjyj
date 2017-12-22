package com.eastsoft.esgjyj.enums;

public enum LatCoefficient {
	XSCoefficient("行申", 0.5),
	MXCoefficient("民辖", 0.9),
	MXZCoefficient("民辖终", 0.9),
	MCCoefficient("民初", 0.9),
	MZCoefficient("民终", 0.9),
	XKCoefficient("刑抗", 0.5),
	XZKCoefficient("行抗", 0.5),
	ZBCoefficient("执保", 1);
	
	
	private final String text;
	private final double value;
	private LatCoefficient(String text, double value) {
		this.text = text;
		this.value = value;
	}
	public String getText() {
		return text;
	}
	public double getValue() {
		return value;
	}
	
}

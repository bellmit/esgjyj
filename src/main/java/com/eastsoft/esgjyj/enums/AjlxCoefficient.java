package com.eastsoft.esgjyj.enums;

/**
 * 案件类型系数对应
 * @author zzx
 *
 */
public enum AjlxCoefficient {
	XJCoefficient("刑监", 0.5),
	MJCoefficient("民监", 0.5),
	XZJCoefficient("行监", 0.5),
	WPJCoefficient("委赔监", 0.5),
	XZCoefficient("刑再", 1),
	MZCoefficient("民再", 1),
	XZZCoefficient("行再", 1),
	MTCoefficient("民提", 1),
	XZTCoefficient("行提", 0.5),
	WPTCoefficient("委赔提", 0.5),
	WPCoefficient("委赔", 1),
	FPCoefficient("法赔", 1),
	SJXCoefficient("司救刑", 1),
	SJMCoefficient("司救民", 1),
	SJXZCoefficient("司救行", 1),
	SJPCoefficient("司救赔", 1),
	SJZCoefficient("司救执", 1),
	SJFCoefficient("司救访", 1),
	XGCoefficient("刑更", 0.07),
	XGBCoefficient("刑更备", 0.07),
	XHCoefficient("刑核", 1),
	XEFCoefficient("刑二复", 1),
	XYFCoefficient("刑一复", 1),
	MCCoefficient("民撤", 1.5),
	PZCoefficient("破终", 1);
	
	private final String text;
	private final double value;
	private AjlxCoefficient(String text, double value) {
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

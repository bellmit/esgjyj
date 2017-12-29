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
	WPJCoefficient("委赔监", 1),
	XZCoefficient("刑再", 1),
	MZCoefficient("民再", 1),
	XZZCoefficient("行再", 1),
	MTCoefficient("民提", 1),
	XZTCoefficient("行提", 0.5),
	WPTCoefficient("委赔提", 1),
	WPCoefficient("委赔", 1),
	FPCoefficient("法赔", 1),
	SJXCoefficient("司救刑", 1),
	SJMCoefficient("司救民", 1),
	SJXZCoefficient("司救行", 1),
	SJPCoefficient("司救赔", 1),
	SJZCoefficient("司救执", 1),
	SJFCoefficient("司救访", 1),
	XGCoefficient("刑更", 0.07),
	XGBCoefficient("刑更备", 1),
	XHCoefficient("刑核", 1),
	XEFCoefficient("刑二复", 1),
	XYFCoefficient("刑一复", 1),
	MCCoefficient("民撤", 1.5),
	PZCoefficient("破终", 1),
	XTCoefficient("刑他", 1),
	ZJCoefficient("执监", 1),
	SCFCoefficient("司惩复", 1),
	ZFCoefficient("执复", 1),
	ZTCoefficient("执他", 1),
	XSFCoefficient("行审复", 1),
	MXZCoefficient("民辖终", 0.9),
	PTCofficient("赔他", 1);
	
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

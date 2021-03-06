package com.eastsoft.esgjyj.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 29, 2017 6:22:58 PM CST
 */
public class YjkhAjmxDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//$column.comments
	private String id;
	//$column.comments
	private String khdxid;
	//$column.comments
	private String colIndex;
	//$column.comments
	private String lb;
	//$column.comments
	private Long caseSn;
	//$column.comments
	private Float score;
	//$column.comments
	private String dfsm;

	private String ah;
	
	private Float averageScore;

	public Float getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(Float averageScore) {
		this.averageScore = averageScore;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setKhdxid(String khdxid) {
		this.khdxid = khdxid;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getKhdxid() {
		return khdxid;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setColIndex(String colIndex) {
		this.colIndex = colIndex;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getColIndex() {
		return colIndex;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setLb(String lb) {
		this.lb = lb;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getLb() {
		return lb;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setCaseSn(Long caseSn) {
		this.caseSn = caseSn;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Long getCaseSn() {
		return caseSn;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setScore(Float score) {
		this.score = score;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Float getScore() {
		return score;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setDfsm(String dfsm) {
		this.dfsm = dfsm;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getDfsm() {
		return dfsm;
	}

	public String getAh() {
		return ah;
	}

	public void setAh(String ah) {
		this.ah = ah;
	}
}

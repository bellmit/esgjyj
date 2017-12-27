package com.eastsoft.esgjyj.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;



/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 22, 2017 1:47:47 PM CST
 */
public class YjkhKgssDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//$column.comments
	private String id;
	//$column.comments
	private String userid;
	//$column.comments
	private String zbid;
	//$column.comments
	private Integer xh;
	//$column.comments
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fssj;
	//$column.comments
	private Float score;
	//$column.comments
	private String note;
	//$column.comments
	private String note1;
	//$column.comments
	private String path;
	//$column.comments
	private String zt;

	private String userName;

	private String zbName;

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
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setZbid(String zbid) {
		this.zbid = zbid;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getZbid() {
		return zbid;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setXh(Integer xh) {
		this.xh = xh;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Integer getXh() {
		return xh;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setFssj(Date fssj) {
		this.fssj = fssj;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Date getFssj() {
		return fssj;
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
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getNote() {
		return note;
	}
	
	public String getNote1() {
		return note1;
	}
	public void setNote1(String note1) {
		this.note1 = note1;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setZt(String zt) {
		this.zt = zt;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getZt() {
		return zt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getZbName() {
		return zbName;
	}

	public void setZbName(String zbName) {
		this.zbName = zbName;
	}
}

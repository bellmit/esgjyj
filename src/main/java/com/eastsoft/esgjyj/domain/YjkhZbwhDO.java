package com.eastsoft.esgjyj.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 15, 2017 10:54:36 AM CST
 */
public class YjkhZbwhDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//$column.comments
	private String id;
	//$column.comments
	private String khid;
	//$column.comments
	private String zbmc;
	//$column.comments
	private Float zbsx;
	//$column.comments
	private String zfz;

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
	public void setKhid(String khid) {
		this.khid = khid;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getKhid() {
		return khid;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setZbmc(String zbmc) {
		this.zbmc = zbmc;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getZbmc() {
		return zbmc;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setZbsx(Float zbsx) {
		this.zbsx = zbsx;
	}
	/**
	 * 获取：${column.comments}
	 */
	public Float getZbsx() {
		return zbsx;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setZfz(String zfz) {
		this.zfz = zfz;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getZfz() {
		return zfz;
	}
}

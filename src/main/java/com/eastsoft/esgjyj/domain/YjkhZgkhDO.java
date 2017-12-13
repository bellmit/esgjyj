package com.eastsoft.esgjyj.domain;

import java.io.Serializable;
import java.util.Date;



/**
 * ${comments}
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date November 13, 2017 5:27:32 PM CST
 */
public class YjkhZgkhDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//$column.comments
	private String id;
	//$column.comments
	private String dxid;
	//$column.comments
	private String zbid;
	//$column.comments
	private Integer xh;
	//$column.comments
	private Date fssj;
	//$column.comments
	private Float score;
	//$column.comments
	private String note;
	//$column.comments
	private String khrlb;
	//$column.comments
	private String khrbs;

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
	public void setDxid(String dxid) {
		this.dxid = dxid;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getDxid() {
		return dxid;
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
	/**
	 * 设置：${column.comments}
	 */
	public void setKhrlb(String khrlb) {
		this.khrlb = khrlb;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getKhrlb() {
		return khrlb;
	}
	/**
	 * 设置：${column.comments}
	 */
	public void setKhrbs(String khrbs) {
		this.khrbs = khrbs;
	}
	/**
	 * 获取：${column.comments}
	 */
	public String getKhrbs() {
		return khrbs;
	}
}

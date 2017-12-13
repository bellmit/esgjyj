package com.eastsoft.esgjyj.domain;

import java.io.Serializable;

public class Office implements Serializable {
	private static final long serialVersionUID = 1641584606097813817L;

	private String ofid;

    private String fullname;

    private String shortname;

    private String oftype;

    private String oflevel;

    private String sftjbh;

    private String state;

    private String header;

    private String courtNo;

    private Integer kblajlx;

    private String zxxtbm;

    private Integer bmbs;

    public String getOfid() {
        return ofid;
    }

    public void setOfid(String ofid) {
        this.ofid = ofid == null ? null : ofid.trim();
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname == null ? null : fullname.trim();
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname == null ? null : shortname.trim();
    }

    public String getOftype() {
        return oftype;
    }

    public void setOftype(String oftype) {
        this.oftype = oftype == null ? null : oftype.trim();
    }

    public String getOflevel() {
        return oflevel;
    }

    public void setOflevel(String oflevel) {
        this.oflevel = oflevel == null ? null : oflevel.trim();
    }

    public String getSftjbh() {
        return sftjbh;
    }

    public void setSftjbh(String sftjbh) {
        this.sftjbh = sftjbh == null ? null : sftjbh.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header == null ? null : header.trim();
    }

    public String getCourtNo() {
        return courtNo;
    }

    public void setCourtNo(String courtNo) {
        this.courtNo = courtNo == null ? null : courtNo.trim();
    }

    public Integer getKblajlx() {
        return kblajlx;
    }

    public void setKblajlx(Integer kblajlx) {
        this.kblajlx = kblajlx;
    }

    public String getZxxtbm() {
        return zxxtbm;
    }

    public void setZxxtbm(String zxxtbm) {
        this.zxxtbm = zxxtbm == null ? null : zxxtbm.trim();
    }

    public Integer getBmbs() {
        return bmbs;
    }

    public void setBmbs(Integer bmbs) {
        this.bmbs = bmbs;
    }

	@Override
	public String toString() {
		return "Office [ofid=" + ofid + ", fullname=" + fullname + ", shortname=" + shortname + ", oftype=" + oftype
				+ ", oflevel=" + oflevel + ", sftjbh=" + sftjbh + ", state=" + state + ", header=" + header
				+ ", courtNo=" + courtNo + ", kblajlx=" + kblajlx + ", zxxtbm=" + zxxtbm + ", bmbs=" + bmbs + "]";
	}
}
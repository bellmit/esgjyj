package com.eastsoft.esgjyj.domain;

public class KhjgKey {
    private String dxid;

    private Integer xh;

    public String getDxid() {
        return dxid;
    }

    public void setDxid(String dxid) {
        this.dxid = dxid == null ? null : dxid.trim();
    }

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }
}
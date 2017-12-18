package com.eastsoft.esgjyj.domain;

import java.math.BigDecimal;

public class Khjg extends KhjgKey {
    private Integer colIndex;

    private String colName;

    private BigDecimal score;

    private String dfsm;

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName == null ? null : colName.trim();
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getDfsm() {
        return dfsm;
    }

    public void setDfsm(String dfsm) {
        this.dfsm = dfsm == null ? null : dfsm.trim();
    }
}
package com.neu.project.entity;

public class FOF {
    private int fofid;
    private String fofname;
    private String dfname;
    private int id;//用户id
    private int fundcode;
    private String fundname;
    private int percent;

    public FOF(int fofid,String fofname,String dfname, int id, int fundcode,String fundname, int percent) {
        this.fofid = fofid;
        this.fofname = fofname;
        this.dfname = dfname;
        this.id = id;
        this.fundcode = fundcode;
        this.fundname = fundname;
        this.percent = percent;
    }
    public FOF() {}

    public int getFofid() {
        return fofid;
    }

    public void setFofid(int fofid) {
        this.fofid = fofid;
    }

    public String getFofname() {
        return fofname;
    }

    public String getDfname() {
        return dfname;
    }

    public void setDfname(String dfname) {
        this.dfname = dfname;
    }

    public void setFofname(String fofname) {
        this.fofname = fofname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFundcode() {
        return fundcode;
    }

    public void setFundcode(int fundcode) {
        this.fundcode = fundcode;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getFundname() {
        return fundname;
    }
    public void setFundname(String fundname) {
        this.fundname = fundname;
    }
}

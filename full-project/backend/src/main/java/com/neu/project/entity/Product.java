package com.neu.project.entity;

public class Product {
    private String fofname;
    private String dfname;
    private int id;//用户id
    private String status;
    private String risk;
    private String description;

    public Product(String fofname,String dfname, int id, String status, String risk, String description) {
        this.fofname = fofname;
        this.id = id;
        this.status = status;
        this.risk = risk;
        this.description = description;
    }
    public Product() {}

    public String getFofname() {
        return fofname;
    }

    public void setFofname(String fofname) {
        this.fofname = fofname;
    }

    public String getDfname() {
        return dfname;
    }

    public void setDfname(String dfname) {
        this.dfname = dfname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

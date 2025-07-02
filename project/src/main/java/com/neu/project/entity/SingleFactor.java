package com.neu.project.entity;

public class SingleFactor {
    private String factorname;
    private String fathername;

    public SingleFactor(String factorname, String fathername) {
        this.factorname = factorname;
        this.fathername = fathername;
    }

    public SingleFactor() {
    }

    public String getFactorname() {
        return factorname;
    }

    public void setFactorname(String factorname) {
        this.factorname = factorname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }
}

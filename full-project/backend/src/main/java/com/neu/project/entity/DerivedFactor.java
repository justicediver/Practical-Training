package com.neu.project.entity;

public class DerivedFactor {
    private String dfname;
    private int id;//表示用户id
    private String factornames;
    private String percents;

    public DerivedFactor(String dfname, int id, String factornames, String percents) {
        this.dfname = dfname;
        this.id = id;
        this.factornames = factornames;
        this.percents = percents;
    }

    public DerivedFactor() {
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

    public String getFactornames() {
        return factornames;
    }

    public void setFactornames(String factornames) {
        this.factornames = factornames;
    }

    public String getPercents() {
        return percents;
    }

    public void setPercents(String percents) {
        this.percents = percents;
    }
}

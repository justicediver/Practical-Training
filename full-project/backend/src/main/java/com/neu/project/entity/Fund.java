package com.neu.project.entity;

public class Fund {
    private int fundcode;
    private String fundname;
    private String company;
    private String manager;
    private double net_worth;
    private double annual_return;
    private double total_return;
    private String info;

    public Fund(int fundcode, String fundname, String company, String manager, double net_worth, double annual_return, double total_return, String info) {
        this.fundcode = fundcode;
        this.fundname = fundname;
        this.company = company;
        this.manager = manager;
        this.net_worth = net_worth;
        this.annual_return = annual_return;
        this.total_return = total_return;
        this.info = info;
    }

    public Fund() {
    }

    public int getFundcode() {
        return fundcode;
    }

    public void setFundcode(int fundcode) {
        this.fundcode = fundcode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public double getNet_worth() {
        return net_worth;
    }

    public void setNet_worth(double net_worth) {
        this.net_worth = net_worth;
    }

    public double getTotal_return() {
        return total_return;
    }

    public void setTotal_return(double total_return) {
        this.total_return = total_return;
    }

    public double getAnnual_return() {
        return annual_return;
    }

    public void setAnnual_return(double annual_return) {
        this.annual_return = annual_return;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

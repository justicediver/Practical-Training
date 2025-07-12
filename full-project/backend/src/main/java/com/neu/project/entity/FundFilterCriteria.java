package com.neu.project.entity;

public class FundFilterCriteria {
    private Integer fundcode;
    private String fundname;
    private String company;
    private String manager;
    private String companySizeLevel;    // 公司规模等级：LARGE, MEDIUM, SMALL
    private String companyRatingLevel;  // 公司评级等级：HIGH, MEDIUM, LOW
    private String companyRiskLevel;    // 公司风险等级：HIGH, MEDIUM, LOW
    private String managerAssetLevel;   // 经理管理规模等级：LARGE, MEDIUM, SMALL
    private String managerReturnLevel;  // 经理回报等级：HIGH, MEDIUM, LOW
    private String managerExpLevel;     // 经理经验等级：SENIOR, MID, JUNIOR

    public FundFilterCriteria() {}

    public FundFilterCriteria(Integer fundcode, String fundname, String company, String manager,
                              String companySizeLevel, String companyRatingLevel, String companyRiskLevel,
                              String managerAssetLevel, String managerReturnLevel, String managerExpLevel) {
        this.fundcode = fundcode;
        this.fundname = fundname;
        this.company = company;
        this.manager = manager;
        this.companySizeLevel = companySizeLevel;
        this.companyRatingLevel = companyRatingLevel;
        this.companyRiskLevel = companyRiskLevel;
        this.managerAssetLevel = managerAssetLevel;
        this.managerReturnLevel = managerReturnLevel;
        this.managerExpLevel = managerExpLevel;
    }

    public Integer getFundcode() {
        return fundcode;
    }

    public void setFundcode(Integer fundcode) {
        this.fundcode = fundcode;
    }

    public String getFundname() {
        return fundname;
    }

    public void setFundname(String fundname) {
        this.fundname = fundname;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCompanySizeLevel() {
        return companySizeLevel;
    }

    public void setCompanySizeLevel(String companySizeLevel) {
        this.companySizeLevel = companySizeLevel;
    }

    public String getCompanyRatingLevel() {
        return companyRatingLevel;
    }

    public void setCompanyRatingLevel(String companyRatingLevel) {
        this.companyRatingLevel = companyRatingLevel;
    }

    public String getCompanyRiskLevel() {
        return companyRiskLevel;
    }

    public void setCompanyRiskLevel(String companyRiskLevel) {
        this.companyRiskLevel = companyRiskLevel;
    }

    public String getManagerAssetLevel() {
        return managerAssetLevel;
    }

    public void setManagerAssetLevel(String managerAssetLevel) {
        this.managerAssetLevel = managerAssetLevel;
    }

    public String getManagerReturnLevel() {
        return managerReturnLevel;
    }

    public void setManagerReturnLevel(String managerReturnLevel) {
        this.managerReturnLevel = managerReturnLevel;
    }

    public String getManagerExpLevel() {
        return managerExpLevel;
    }

    public void setManagerExpLevel(String managerExpLevel) {
        this.managerExpLevel = managerExpLevel;
    }
}
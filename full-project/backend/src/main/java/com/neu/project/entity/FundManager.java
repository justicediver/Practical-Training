package com.neu.project.entity;

import java.math.BigDecimal;

public class FundManager {
    private int id;                   // 基金经理ID（主元素）
    private String name;              // 基金经理姓名
    private BigDecimal managedAssets; // 管理资产规模（1到100（单位：亿））
    private double annualReturnRate;  // 年化回报率（1到30（%））
    private int careerAge;            // 从业年限（1到10年）

    public FundManager(int id, String name, BigDecimal managedAssets,
                       double annualReturnRate, int careerAge) {
        this.id = id;
        this.name = name;
        this.managedAssets = managedAssets;
        this.annualReturnRate = annualReturnRate;
        this.careerAge = careerAge;
    }

    public FundManager() {}

    // Getter和Setter方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getManagedAssets() {
        return managedAssets;
    }

    public void setManagedAssets(BigDecimal managedAssets) {
        this.managedAssets = managedAssets;
    }

    public double getAnnualReturnRate() {
        return annualReturnRate;
    }

    public void setAnnualReturnRate(double annualReturnRate) {
        this.annualReturnRate = annualReturnRate;
    }

    public int getCareerAge() {
        return careerAge;
    }

    public void setCareerAge(int careerAge) {
        this.careerAge = careerAge;
    }
}

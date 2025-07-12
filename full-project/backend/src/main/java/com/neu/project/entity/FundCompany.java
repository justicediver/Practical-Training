package com.neu.project.entity;

import java.math.BigDecimal;

public class FundCompany {
    private String name;              // 基金公司名称（主元素）
    private BigDecimal totalAssets;   // 资产管理规模,范围是1到500（单位：亿）
    private int starRating;           // 星级评级（1到5）
    private String riskLevel;         // 风险等级（低风险，中风险，高风险）

    public FundCompany(String name, BigDecimal totalAssets, int starRating, String riskLevel) {
        this.name = name;
        this.totalAssets = totalAssets;
        this.starRating = starRating;
        this.riskLevel = riskLevel;
    }

    public FundCompany() {}

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(BigDecimal totalAssets) {
        this.totalAssets = totalAssets;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
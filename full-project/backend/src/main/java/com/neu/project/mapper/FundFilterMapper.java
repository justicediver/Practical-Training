package com.neu.project.mapper;

import com.neu.project.entity.Fund;
import com.neu.project.entity.FundFilterCriteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface FundFilterMapper {

    @Select("<script>" +
            "SELECT f.* FROM fund f " +
            "LEFT JOIN fund_company fc ON f.company = fc.name " +
            "LEFT JOIN fund_manager fm ON f.manager = fm.name " +
            "WHERE 1=1 " +
            "<if test='fundcode != null'> AND f.fundcode = #{fundcode} </if>" +
            "<if test='fundname != null'> AND f.fundname LIKE CONCAT('%', #{fundname}, '%') </if>" +
            "<if test='company != null'> AND f.company LIKE CONCAT('%', #{company}, '%') </if>" +
            "<if test='manager != null'> AND f.manager LIKE CONCAT('%', #{manager}, '%') </if>" +

            // 公司规模筛选条件（修正单位：亿）
            "<if test='companySizeLevel != null'> " +
            "AND (" +
            "  (#{companySizeLevel} = 'LARGE' AND fc.total_assets >= 100) " +
            "  OR (#{companySizeLevel} = 'MEDIUM' AND fc.total_assets >= 50 AND fc.total_assets &lt; 100) " +
            "  OR (#{companySizeLevel} = 'SMALL' AND fc.total_assets &lt; 50)" +
            ") " +
            "</if>" +

            // 公司评级筛选条件
            "<if test='companyRatingLevel != null'> " +
            "AND (" +
            "  (#{companyRatingLevel} = 'HIGH' AND fc.star_rating >= 4) " +
            "  OR (#{companyRatingLevel} = 'MEDIUM' AND fc.star_rating = 3) " +
            "  OR (#{companyRatingLevel} = 'LOW' AND fc.star_rating &lt;= 2)" + // 注意：这里的小于等于号，小于号需要转义
            ") " +
            "</if>" +

            // 公司风险等级筛选条件（中英文转换）
            "<if test='companyRiskLevel != null'> " +
            "AND (" +
            "  (#{companyRiskLevel} = 'HIGH' AND fc.risk_level = '高') " +
            "  OR (#{companyRiskLevel} = 'MEDIUM' AND fc.risk_level = '中') " +
            "  OR (#{companyRiskLevel} = 'LOW' AND fc.risk_level = '低')" +
            ") " +
            "</if>" +

            // 经理管理规模筛选条件（修正单位：亿）
            "<if test='managerAssetLevel != null'> " +
            "AND (" +
            "  (#{managerAssetLevel} = 'LARGE' AND fm.managed_assets >= 50) " +
            "  OR (#{managerAssetLevel} = 'MEDIUM' AND fm.managed_assets >= 10 AND fm.managed_assets &lt; 50) " +
            "  OR (#{managerAssetLevel} = 'SMALL' AND fm.managed_assets &lt; 10)" +
            ") " +
            "</if>" +

            // 经理回报率筛选条件
            "<if test='managerReturnLevel != null'> " +
            "AND (" +
            "  (#{managerReturnLevel} = 'HIGH' AND fm.annual_return_rate >= 15) " +
            "  OR (#{managerReturnLevel} = 'MEDIUM' AND fm.annual_return_rate >= 8 AND fm.annual_return_rate &lt; 15) " +
            "  OR (#{managerReturnLevel} = 'LOW' AND fm.annual_return_rate &lt; 8)" +
            ") " +
            "</if>" +

            // 经理经验筛选条件
            "<if test='managerExpLevel != null'> " +
            "AND (" +
            "  (#{managerExpLevel} = 'SENIOR' AND fm.career_age >= 10) " +
            "  OR (#{managerExpLevel} = 'MID' AND fm.career_age >= 5 AND fm.career_age &lt; 10) " +
            "  OR (#{managerExpLevel} = 'JUNIOR' AND fm.career_age &lt; 5)" +
            ") " +
            "</if>" +
            "</script>")
    List<Fund> filterFunds(FundFilterCriteria criteria);
}
package com.neu.project.mapper;

import com.neu.project.entity.Fund;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface FundMapper {

    // 根据基金代码精确查询
    @Select("SELECT * FROM fund WHERE fundcode = #{fundcode}")
    Fund findByFundCode(int fundcode);

    // 根据基金名称模糊查询
    @Select("SELECT * FROM fund WHERE fundname LIKE CONCAT('%', #{fundname}, '%')")
    List<Fund> findByFundName(String fundname);

    // 根据公司名称模糊查询
    @Select("SELECT * FROM fund WHERE company LIKE CONCAT('%', #{company}, '%')")
    List<Fund> findByCompany(String company);

    // 根据基金经理模糊查询
    @Select("SELECT * FROM fund WHERE manager LIKE CONCAT('%', #{manager}, '%')")
    List<Fund> findByManager(String manager);

    // 组合查询（支持模糊查询）
    @Select("<script>" +
            "SELECT * FROM fund WHERE 1=1 " +
            "<if test='fundcode != null'> AND fundcode = #{fundcode} </if>" +
            "<if test='fundname != null'> AND fundname LIKE CONCAT('%', #{fundname}, '%') </if>" +
            "<if test='company != null'> AND company LIKE CONCAT('%', #{company}, '%') </if>" +
            "<if test='manager != null'> AND manager LIKE CONCAT('%', #{manager}, '%') </if>" +
            "</script>")
    List<Fund> findByCondition(Integer fundcode, String fundname, String company, String manager);

    @Select("SELECT * FROM fund")
    List<Fund> findAll();
}
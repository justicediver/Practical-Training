package com.neu.project.mapper;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.SingleFactor;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FactorMapper {

    @Insert("INSERT INTO derivedfactor(dfname, id, factornames, percents) " +
            "VALUES(#{dfname}, #{id}, #{factornames}, #{percents})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertDerivedFactor(DerivedFactor factor);

    @Update("UPDATE derivedfactor SET factornames=#{factornames}, percents=#{percents} " +
            "WHERE dfname=#{dfname} AND id=#{id}")
    int updateDerivedFactor(DerivedFactor factor);

    @Delete("DELETE FROM derivedfactor WHERE dfname=#{dfname} AND id=#{id}")
    int deleteDerivedFactor(@Param("dfname") String dfname, @Param("id") int id);

    @Select("SELECT * FROM derivedfactor WHERE id=#{userId}")
    List<DerivedFactor> getDerivedFactorsByUser(int userId);

    @Select("SELECT * FROM derivedfactor WHERE dfname=#{dfname} AND id=#{userId}")
    DerivedFactor getDerivedFactorByNameAndUser(
            @Param("dfname") String dfname,
            @Param("userId") int userId);

    @Select("SELECT * FROM singlefactor WHERE fathername=#{fathername}")
    List<SingleFactor> getSingleFactorsByFather(String fathername);

    @Select("SELECT * FROM singlefactor WHERE factorname=#{factorname}")
    SingleFactor getSingleFactorByName(String factorname);

    @Select("select distinct fathername from singlefactor")
    List<String> getAllfather();
}
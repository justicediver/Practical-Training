package com.neu.project.mapper;

import com.neu.project.entity.DerivedFactor;
import com.neu.project.entity.FOF;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FOFMapper {
    @Insert("insert into fof values (null, 'name', null, #{id}, #{fundcode}, #{fundname}, 0)")
    void insertFundIntoFOF(int id, int fundcode, String fundname);

    @Delete("delete from fof where id = #{id} and fundcode = #{fundcode}")
    void deleteFundIntoFOF(int id, int fundcode);

    @Select("select case when exists(select * from fof where id = #{id} and fundcode = #{fundcode}) " +
            "then 1 else 0 end")
    int existsFOF(int id, int fundcode);

    // 获取用户的所有基金配置
    @Select("SELECT fo.fundcode, f.fundname, fo.percent FROM fof fo " +
            "JOIN fund f ON fo.fundcode = f.fundcode " +
            "WHERE fo.id = #{id} AND fo.fofname = 'name'")
    List<FOF> findUserFundConfigurations(int id);

    // 更新基金权重
    @Update("UPDATE fof SET percent = #{percent} WHERE id = #{id} AND fundcode = #{fundcode} AND fofname = 'name'")
    void updateFundPercentage(int id, int fundcode, int percent);

    @Select("SELECT dfname, factornames, percents FROM derivedfactor WHERE id = #{id}")
    List<DerivedFactor> findUserDerivedFactors(int id);
}
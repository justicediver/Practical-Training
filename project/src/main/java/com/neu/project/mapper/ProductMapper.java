package com.neu.project.mapper;

import com.neu.project.entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper {
    @Insert("insert into product values (#{fofname},#{dfname},#{id},'未上架',null,null)")
    void init(String fofname,String dfname,int id);

    @Update("update product set status = '审核中',risk = #{risk},description = #{description} " +
            "where id = #{id} and fofname = #{fofname}")
    void update(String fofname,int id,String risk,String description);

    @Select("select * from product where id = #{id}")
    List<Product> findById(int id);
}
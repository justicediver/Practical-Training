package com.neu.project.mapper;

import com.neu.project.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select pw from user where username = #{username}")
    String findPwByName(String username);

    @Select("select user.id from user where username = #{username}")
    int findIdByName(String username);

    @Select("select * from user where id = #{id}")
    User findUserById(int id);

    @Update("update user set pw = #{pw} ," +
            "username =#{username}," +
            " where id = #{id}")
    void updateUser(User user);

    @Select("select case when exists(select username from user where id != #{id} and username = #{username})" +
            " then 1" +
            " else 0" +
            " END")
    int findNameExist(int id, String username);

    @Insert("insert into user values (null,#{pw},#{username}) ")
    void insertUser(User user);

    @Select("select * from user")
    List<User> getAllUser();

    @Update("UPDATE user SET username = #{username}, pw = #{pw} WHERE id = #{id}")
    void updateUserWithoutCheck(User user);
}

package com.swan.demo.mapper;

import com.swan.demo.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("""
            select *
            from user
            where id = #{id}
            """)
    User findById(Long id);

    @Insert("""
             insert into user
                (
                    username,
                    password,
                    nickname
                )
                values
                (
                    #{username},
                    #{password},
                    #{nickname}
                )
            """)
    @Options(
        useGeneratedKeys = true,
        keyProperty = "id"
    )
    int insert(User user);

    @Select("""
            select *
            from user
            order by id
            """)
    List<User> findAll();

    @Select("""
            select *
            from user
            order by id
            limit #{offset}, #{size}
            """)
    List<User> findPage(
            Long offset,
            Integer size
    );

    @Select("""
            select count(*)
            from user
            """)
    Long count();

    @Select("""
            select *
            from user
            where username = #{username}
            """)
    User findByUsername(String username);
}

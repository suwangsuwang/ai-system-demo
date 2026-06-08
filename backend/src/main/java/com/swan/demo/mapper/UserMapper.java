package com.swan.demo.mapper;

import com.swan.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("""
            select *
            from user
            where id = #{id}
            """)
    User findById(Long id);
}

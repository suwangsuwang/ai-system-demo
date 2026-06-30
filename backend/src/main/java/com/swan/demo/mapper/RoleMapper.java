package com.swan.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("""
            SELECT r.code
                    FROM user_role ur
                    JOIN role r ON ur.role_id = r.id
                    WHERE ur.user_id = #{userId}
            """)
    List<String> findRoleByUserId(Long userId);
}

package com.swan.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Select("""
            SELECT DISTINCT p.code
                    FROM user_role ur
                    JOIN role_permission rp ON ur.role_id = rp.role_id
                    JOIN permission p ON rp.permission_id = p.id
                    WHERE ur.user_id = #{userId}
            """)
    List<String> findPermissionByUserId(Long userId);
}

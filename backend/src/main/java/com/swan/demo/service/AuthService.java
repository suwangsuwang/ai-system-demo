package com.swan.demo.service;

import com.swan.demo.entity.User;
import com.swan.demo.mapper.PermissionMapper;
import com.swan.demo.mapper.RoleMapper;
import com.swan.demo.mapper.UserMapper;
import com.swan.demo.util.JwtUtil;
import com.swan.demo.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    public Set<String> getUserPermissions(Long userId) {

        List<String> perms = permissionMapper.findPermissionByUserId(userId);

        return new HashSet<>(perms);
    }

    public String login(String username, String password) {

        User user = userMapper.findByUsername(username);

        List<String> perms = permissionMapper.findPermissionByUserId(user.getId());

        // 1. 写 Redis
        redisUtil.set("perm:" + user.getId(), perms, 3600);

        redisUtil.set("user:" + user.getId(), user, 3600);

        // 2. 生成 JWT (只放 userId)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        return JwtUtil.createToken(claims);
    }
}

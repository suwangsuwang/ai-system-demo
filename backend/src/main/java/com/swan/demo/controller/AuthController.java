package com.swan.demo.controller;

import com.swan.demo.common.Result;
import com.swan.demo.dto.LoginRequest;
import com.swan.demo.entity.User;
import com.swan.demo.service.UserService;
import com.swan.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<String> login(
            @RequestBody LoginRequest request
            ) {
        User user = userService.findByUsername(request.getUsername());

        if (user == null) {
            return Result.fail("用户不存在");
        }

        if (!user.getPassword().equals(request.getPassword())) {
            return Result.fail("密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("userId", user.getId());
        String token = JwtUtil.createToken(claims);
        return Result.ok(token);

    }
}

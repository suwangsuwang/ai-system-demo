package com.swan.demo.controller;


import com.swan.demo.common.PageResult;
import com.swan.demo.common.Result;
import com.swan.demo.context.UserContext;
import com.swan.demo.dto.CreateUserRequest;
import com.swan.demo.vo.UserVO;
import com.swan.demo.entity.User;
import com.swan.demo.service.UserService;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.ok(userService.findById(id));
    }

    @PostMapping
    public Result<Long> createUser(@RequestBody CreateUserRequest request) {

        return Result.ok(userService.createUser(request));
    }

    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.ok(userService.findAll());
    }

    @GetMapping("/page")
    public Result<PageResult<User>> page(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        PageResult<User> result = new PageResult<>();
        result.setPage(page);
        result.setSize(size);

        result.setTotal(
                userService.count()
        );
        result.setRecords(
                userService.findPage(page, size)
        );
        return Result.ok(
                result
        );
    }

    @GetMapping("/me")
    public Result<UserVO> me() {

        Long userId = UserContext.get();

        return Result.ok(userService.findById(userId));
    }

}

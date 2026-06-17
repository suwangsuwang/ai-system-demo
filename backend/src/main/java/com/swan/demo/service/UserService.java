package com.swan.demo.service;

import com.swan.demo.dto.CreateUserRequest;
import com.swan.demo.entity.User;
import com.swan.demo.mapper.UserMapper;
import com.swan.demo.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public UserVO findById(Long id) {

        String key = "user:" + id;

        UserVO cache = (UserVO) redisTemplate.opsForValue().get(key);

        if (cache != null) {

            System.out.println("🔥 FROM REDIS");

            return cache;
        }

        System.out.println("🔥 FROM MYSQL");

        User user = userMapper.findById(id);

        if (user == null) {
            return null;
        }

        UserVO vo = UserVO.from(user);

        redisTemplate.opsForValue().set(key, vo, Duration.ofMinutes(30));

        return vo;
    }

    public Long createUser(CreateUserRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());

        userMapper.insert(user);

        return user.getId();
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public List<User> findPage(
            Integer page,
            Integer size
    ) {
        long offset = (long) (page -1) * size;

        return userMapper.findPage(offset, size);
    }

    public Long count() {
        return userMapper.count();
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

}

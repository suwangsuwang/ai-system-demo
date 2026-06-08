package com.swan.demo.service;

import com.swan.demo.dto.CreateUserRequest;
import com.swan.demo.dto.UserDTO;
import com.swan.demo.entity.User;
import com.swan.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserDTO findById(Long id) {
        User user =  userMapper.findById(id);

        return toDTO(user);
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


    private UserDTO toDTO(User user) {

        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());

        return dto;
    }
}

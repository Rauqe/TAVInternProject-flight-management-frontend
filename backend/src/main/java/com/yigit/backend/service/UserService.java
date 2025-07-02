package com.yigit.backend.service;

import com.yigit.backend.User;
import com.yigit.backend.UserRepository;
import com.yigit.backend.dto.UserDto;
import com.yigit.backend.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(userDto.getUsername());
        user.setRole(userDto.getRole());
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }
} 
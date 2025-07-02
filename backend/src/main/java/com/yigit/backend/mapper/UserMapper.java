package com.yigit.backend.mapper;

import com.yigit.backend.User;
import com.yigit.backend.dto.RegisterRequest;
import com.yigit.backend.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Password will be encoded separately
    User toEntity(RegisterRequest request);

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);
} 
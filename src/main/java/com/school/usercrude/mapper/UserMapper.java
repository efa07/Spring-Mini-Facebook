package com.school.usercrude.mapper;

import com.school.usercrude.dto.request.UserRequestDTO;
import com.school.usercrude.dto.response.UserResponseDTO;
import com.school.usercrude.entity.User;

public class UserMapper {
    private UserMapper() {
      
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
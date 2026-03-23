package com.school.usercrude.service;
import com.school.usercrude.dto.request.UserRequestDTO;
import com.school.usercrude.dto.response.UserResponseDTO;
import java.util.List;

public interface UserService {
    UserResponseDTO createUser(UserRequestDTO dto);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    void deleteUser(Long id);
    UserResponseDTO updateUser(Long id, UserRequestDTO dto);
}

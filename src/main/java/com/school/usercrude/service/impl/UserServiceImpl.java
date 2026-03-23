package com.school.usercrude.service.impl;
import com.school.usercrude.dto.request.UserRequestDTO;
import com.school.usercrude.dto.response.UserResponseDTO;
import com.school.usercrude.entity.User;
import com.school.usercrude.exception.UserAlreadyExistsException;
import com.school.usercrude.mapper.UserMapper;
import com.school.usercrude.repository.UserRepository;
import com.school.usercrude.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = UserMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponseDTO(savedUser); 
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(UserMapper::toResponseDTO)
            .toList();
    }       

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return UserMapper.toResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (!Objects.equals(existingUser.getEmail(), dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(dto.getPassword());

        User savedUser = userRepository.save(existingUser);
        return UserMapper.toResponseDTO(savedUser);
    }

}
package com.school.usercrude.service.impl;
import com.school.usercrude.dto.request.UserRequestDTO;
import com.school.usercrude.dto.response.UserResponseDTO;
import com.school.usercrude.entity.Post;
import com.school.usercrude.entity.User;
import com.school.usercrude.exception.ResourceNotFoundException;
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
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        User user = userMapper.toEntity(dto);
        attachPostsToUser(user);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDTO(savedUser);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::toResponseDTO)
            .toList();
    }       

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!Objects.equals(existingUser.getEmail(), dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }

        userMapper.updateEntityFromDto(dto, existingUser);
        attachPostsToUser(existingUser);

        User savedUser = userRepository.save(existingUser);
        return userMapper.toResponseDTO(savedUser);
    }

    private void attachPostsToUser(User user) {
        if (user.getPosts() == null) {
            return;
        }

        for (Post post : user.getPosts()) {
            post.setUser(user);
        }
    }

}
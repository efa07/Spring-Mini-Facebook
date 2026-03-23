package com.school.usercrude.service;

import com.school.usercrude.dto.request.CreatePostRequestDTO;
import com.school.usercrude.dto.response.PostResponseDTO;

public interface PostService {
    PostResponseDTO createPostForUser(Long userId, CreatePostRequestDTO dto);
}
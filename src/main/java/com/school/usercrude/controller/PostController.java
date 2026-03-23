package com.school.usercrude.controller;

import com.school.usercrude.dto.request.CreatePostRequestDTO;
import com.school.usercrude.dto.response.PostResponseDTO;
import com.school.usercrude.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/users/{userId}/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @PathVariable Long userId,
            @Valid @RequestBody CreatePostRequestDTO dto) {
        PostResponseDTO createdPost = postService.createPostForUser(userId, dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{postId}")
            .buildAndExpand(createdPost.getId())
            .toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
            .location(location)
            .body(createdPost);
    }
}
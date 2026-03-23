package com.school.usercrude.service.impl;

import com.school.usercrude.dto.request.CreatePostRequestDTO;
import com.school.usercrude.dto.response.PostResponseDTO;
import com.school.usercrude.entity.Post;
import com.school.usercrude.entity.Tag;
import com.school.usercrude.entity.User;
import com.school.usercrude.exception.ResourceNotFoundException;
import com.school.usercrude.mapper.UserMapper;
import com.school.usercrude.repository.PostRepository;
import com.school.usercrude.repository.TagRepository;
import com.school.usercrude.repository.UserRepository;
import com.school.usercrude.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final UserMapper userMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, TagRepository tagRepository, UserMapper userMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public PostResponseDTO createPostForUser(Long userId, CreatePostRequestDTO dto) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        CreatePostRequestDTO postRequest = new CreatePostRequestDTO(dto.getTitle(), dto.getContent(), dto.getTags());
        Post post = userMapper.toPostEntity(postRequest);
        post.setUser(user);
        post.setTags(resolveTags(dto.getTags()));

        Post savedPost = postRepository.save(post);
        return userMapper.toPostResponseDTO(savedPost);
    }

    private List<Tag> resolveTags(List<String> requestedTags) {
        if (requestedTags == null || requestedTags.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> normalizedNames = requestedTags.stream()
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(name -> !name.isEmpty())
            .collect(Collectors.toCollection(LinkedHashSet::new));

        if (normalizedNames.isEmpty()) {
            return new ArrayList<>();
        }

        List<Tag> existingTags = tagRepository.findByNameIn(normalizedNames);
        Map<String, Tag> existingByName = existingTags.stream()
            .collect(Collectors.toMap(Tag::getName, Function.identity()));

        List<Tag> resolvedTags = new ArrayList<>();
        for (String tagName : normalizedNames) {
            Tag existing = existingByName.get(tagName);
            if (existing != null) {
                resolvedTags.add(existing);
                continue;
            }

            Tag newTag = new Tag();
            newTag.setName(tagName);
            resolvedTags.add(newTag);
        }

        return resolvedTags;
    }
}
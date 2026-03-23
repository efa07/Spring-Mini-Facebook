package com.school.usercrude.service;

import com.school.usercrude.dto.request.TagRequestDTO;
import com.school.usercrude.dto.response.TagResponseDTO;

import java.util.List;

public interface TagsService {
    TagResponseDTO createTag(TagRequestDTO dto);

    List<TagResponseDTO> getAllTags();

    TagResponseDTO getTagById(Long id);

    TagResponseDTO updateTag(Long id, TagRequestDTO dto);

    void deleteTag(Long id);
}
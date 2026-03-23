package com.school.usercrude.service.impl;

import com.school.usercrude.dto.request.TagRequestDTO;
import com.school.usercrude.dto.response.TagResponseDTO;
import com.school.usercrude.entity.Tag;
import com.school.usercrude.exception.ResourceNotFoundException;
import com.school.usercrude.exception.TagAlreadyExistsException;
import com.school.usercrude.repository.TagRepository;
import com.school.usercrude.service.TagsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagsServiceImpl implements TagsService {

    private final TagRepository tagRepository;

    public TagsServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    @Transactional
    public TagResponseDTO createTag(TagRequestDTO dto) {
        String normalizedName = normalizeTagName(dto.getName());
        if (tagRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new TagAlreadyExistsException("Tag already exists with name: " + normalizedName);
        }

        Tag tag = new Tag();
        tag.setName(normalizedName);

        Tag savedTag = tagRepository.save(tag);
        return toResponse(savedTag);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        return toResponse(tag);
    }

    @Override
    @Transactional
    public TagResponseDTO updateTag(Long id, TagRequestDTO dto) {
        Tag existingTag = tagRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));

        String normalizedName = normalizeTagName(dto.getName());
        tagRepository.findByNameIgnoreCase(normalizedName)
            .filter(tag -> !tag.getId().equals(id))
            .ifPresent(tag -> {
                throw new TagAlreadyExistsException("Tag already exists with name: " + normalizedName);
            });

        existingTag.setName(normalizedName);
        Tag savedTag = tagRepository.save(existingTag);
        return toResponse(savedTag);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    private TagResponseDTO toResponse(Tag tag) {
        return new TagResponseDTO(tag.getId(), tag.getName());
    }

    private String normalizeTagName(String rawName) {
        return rawName == null ? null : rawName.trim();
    }
}

package com.school.usercrude.mapper;

import com.school.usercrude.dto.request.UserRequestDTO;
import com.school.usercrude.dto.request.CreatePostRequestDTO;
import com.school.usercrude.dto.response.PostResponseDTO;
import com.school.usercrude.dto.response.UserResponseDTO;
import com.school.usercrude.entity.Post;
import com.school.usercrude.entity.Tag;
import com.school.usercrude.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserResponseDTO toResponseDTO(User user);

    PostResponseDTO toPostResponseDTO(Post post);

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post toPostEntity(CreatePostRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(UserRequestDTO dto, @MappingTarget User user);

    default String map(Tag tag) {
        return tag == null ? null : tag.getName();
    }
}
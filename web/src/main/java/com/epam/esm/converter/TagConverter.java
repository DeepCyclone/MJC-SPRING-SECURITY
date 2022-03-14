package com.epam.esm.converter;

import java.util.List;

import com.epam.esm.dto.request.TagDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.repository.model.Tag;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagConverter {
    Tag convertFromRequestDto(TagDto dto);
    List<Tag> convertFromRequestDtos(List<TagDto> dtos);
    TagResponseDto convertToResponseDto(Tag object);
    List<TagResponseDto> convertToResponseDtos(List<Tag> objects);
}

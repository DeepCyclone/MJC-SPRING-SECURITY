package com.epam.esm.converter;

import com.epam.esm.dto.request.UserDto;
import com.epam.esm.repository.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    User convertFromRequestDto(UserDto dto);
    List<User> convertFromRequestDtos(List<UserDto> dtos);
}

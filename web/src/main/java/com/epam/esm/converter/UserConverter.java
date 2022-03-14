package com.epam.esm.converter;

import java.util.List;

import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.repository.model.User;

import org.mapstruct.Mapper;


@Mapper(componentModel = "spring",uses = OrderConverter.class)
public interface UserConverter {
    UserResponseDto toResponseDto(User user);
    List<UserResponseDto> toResponseDtos(List<User> users);
}

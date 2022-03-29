package com.epam.esm.converter;

import java.util.List;

import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",uses = OrderConverter.class)
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserResponseDto toResponseDto(User user);
    List<UserResponseDto> toResponseDtos(List<User> users);
    UserModel toModel(User user);
    List<UserModel> toModels(List<User> users);
}
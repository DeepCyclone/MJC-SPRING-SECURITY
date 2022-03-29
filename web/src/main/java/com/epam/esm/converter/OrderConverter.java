package com.epam.esm.converter;

import java.util.List;

import com.epam.esm.dto.request.OrderDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.repository.model.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = CertificateConverter.class)
public interface OrderConverter {

    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    OrderResponseDto convertToResponseDto(Order object);
    List<OrderResponseDto> convertToResponseDtos(List<Order> objects);
    @Mapping(target = "purchaseDate",ignore = true)
    Order convertFromRequestDto(OrderDto dto);
    List<Order> convertFromRequestDtos(List<OrderDto> dtos);
}

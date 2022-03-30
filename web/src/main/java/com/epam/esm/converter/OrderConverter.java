package com.epam.esm.converter;

import com.epam.esm.dto.request.OrderDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.repository.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = CertificateConverter.class)
public interface OrderConverter {

    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);

    OrderResponseDto convertToResponseDto(Order object);
    List<OrderResponseDto> convertToResponseDtos(List<Order> objects);
    @Mapping(target = "purchaseDate",ignore = true)
    Order convertFromRequestDto(OrderDto dto);
}

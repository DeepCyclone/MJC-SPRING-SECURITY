package com.epam.esm.converter;

import java.util.List;

import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.repository.model.Order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = CertificateConverter.class)
public interface OrderConverter {
    OrderResponseDto convertToResponseDto(Order object);
    List<OrderResponseDto> convertToResponseDtos(List<Order> objects);

}

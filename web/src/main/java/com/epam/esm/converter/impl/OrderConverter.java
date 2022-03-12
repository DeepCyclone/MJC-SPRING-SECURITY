package com.epam.esm.converter.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.repository.model.Order;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderConverter {
    OrderResponseDto convertToResponseDto(Order object);
    List<OrderResponseDto> convertToResponseDtos(List<Order> objects);

    default Date map(Timestamp timestamp){
        return timestamp==null?null:new Date(timestamp.getNanos());
    }
}

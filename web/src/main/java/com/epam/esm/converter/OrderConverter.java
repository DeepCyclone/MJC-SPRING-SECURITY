package com.epam.esm.converter;

import com.epam.esm.dto.request.OrderDto;
import com.epam.esm.repository.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring",uses = CertificateConverter.class)
public interface OrderConverter {

    OrderConverter INSTANCE = Mappers.getMapper(OrderConverter.class);//TODO поправить,если без этого работает

    @Mapping(target = "purchaseDate",ignore = true)
    @Mapping(source = "certificates" , target = "associatedCertificates")
    Order convertFromRequestDto(OrderDto dto);
}

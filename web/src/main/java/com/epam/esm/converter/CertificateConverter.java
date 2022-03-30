package com.epam.esm.converter;

import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.repository.model.GiftCertificate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring",uses = TagConverter.class)
public interface CertificateConverter {

    CertificateConverter INSTANCE = Mappers.getMapper(CertificateConverter.class);

    @Mapping(target = "createDate",ignore = true)
    @Mapping(target = "lastUpdateDate",ignore = true)
    GiftCertificate convertFromRequestDto(GiftCertificateDto dto);
    List<GiftCertificate> convertFromRequestDtos(List<GiftCertificateDto> dtos);
    GiftCertificateResponseDto convertToResponseDto(GiftCertificate object);
    List<GiftCertificateResponseDto> convertToResponseDtos(List<GiftCertificate> objects);

    default Date map(Timestamp timestamp){
        return timestamp==null?null:new Date(timestamp.getTime());
    }
}

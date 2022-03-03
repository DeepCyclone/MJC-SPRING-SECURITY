package com.epam.esm.converter.impl;

import com.epam.esm.converter.ConverterTemplate;
import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateConverter implements ConverterTemplate<GiftCertificate, GiftCertificateDto, GiftCertificateResponseDto> {


    private final TagService tagService;
    private final TagConverter tagConverter;
    @Autowired
    public CertificateConverter(TagService tagService, TagConverter tagConverter) {

        this.tagService = tagService;
        this.tagConverter = tagConverter;
    }

    @Override
    public GiftCertificate convertFromRequestDto(GiftCertificateDto dto) {
        GiftCertificate certificate = new GiftCertificate();
        if (dto.getId() != null) {
            certificate.setId(dto.getId());
        }
        certificate.setName(dto.getName());
        certificate.setDescription(dto.getDescription());
        certificate.setPrice(dto.getPrice());
        certificate.setDuration(dto.getDuration());
        certificate.setAssociatedTags(tagConverter.convertFromRequestDtos(dto.getAssociatedTags()));
        return certificate;
    }

    @Override
    public List<GiftCertificate> convertFromRequestDtos(List<GiftCertificateDto> dtos) {
        if(dtos == null){
            return Collections.emptyList();
        }
        return dtos.stream().map(this::convertFromRequestDto).collect(Collectors.toList());
    }

    @Override
    public GiftCertificateResponseDto convertToResponseDto(GiftCertificate object) {
        return GiftCertificateResponseDto.builder().
                id(object.getId()).
                name(object.getName()).
                description(object.getDescription()).
                price(object.getPrice()).
                duration(object.getDuration()).
                createDate(new Date(object.getCreateDate().getTime())).
                lastUpdateDate(new Date(object.getLastUpdateDate().getTime())).
                associatedTags(tagConverter.convertToResponseDtos(object.getAssociatedTags())).
                build();
    }

    @Override
    public List<GiftCertificateResponseDto> convertToResponseDtos(List<GiftCertificate> objects) {
        if(objects == null){
            return Collections.emptyList();
        }
        return objects.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }
}

package com.epam.esm.converter.impl;

import com.epam.esm.converter.ConverterTemplate;
import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
        List<GiftCertificate> objects = new ArrayList<>();
        for(GiftCertificateDto gc:dtos){
            objects.add(convertFromRequestDto(gc));
        }
        return objects;
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
        List<GiftCertificateResponseDto> dtos = new ArrayList<>();
        for(GiftCertificate gc:objects){
            dtos.add(convertToResponseDto(gc));
        }
        return dtos;
    }
}

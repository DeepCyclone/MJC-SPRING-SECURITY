package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.hateoas.LinksBuilder;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateControllerLinkBuilder implements LinksBuilder<GiftCertificateResponseDto> {

    @Override
    public void buildLinks(GiftCertificateResponseDto response) {
        response.add(linkTo(methodOn(GiftCertificateController.class).getByID(response.getId())).withSelfRel());
        response.getAssociatedTags().forEach(tag->response.add(linkTo(methodOn(TagController.class).getByID(tag.getId())).withRel("tags")));
    }
    
}

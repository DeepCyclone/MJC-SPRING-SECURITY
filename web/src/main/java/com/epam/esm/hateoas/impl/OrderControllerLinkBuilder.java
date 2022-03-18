package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.hateoas.LinksBuilder;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderControllerLinkBuilder implements LinksBuilder<OrderResponseDto> {


    @Override
    public void buildLinks(OrderResponseDto response) {
        response.add(linkTo(methodOn(OrderController.class).getById(response.getId())).withSelfRel());
        response.getCertificates().forEach(cert->cert.add(linkTo(methodOn(GiftCertificateController.class).getByID(cert.getId())).withRel("certificates")));
    }
    
}

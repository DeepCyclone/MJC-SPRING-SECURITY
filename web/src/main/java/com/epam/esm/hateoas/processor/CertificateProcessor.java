package com.epam.esm.hateoas.processor;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.controller.GiftCertificateController;
import com.epam.esm.hateoas.model.CertificateModel;

import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CertificateProcessor implements RepresentationModelProcessor<CertificateModel> {

    @Override
    public CertificateModel process(CertificateModel model) {
        return model.add(linkTo(methodOn(GiftCertificateController.class).getByID(model.getId() + 1)).withRel("next")).
        add(linkTo(methodOn(GiftCertificateController.class).getByID(model.getId() - 1)).withRel("prev"));
    }
    
}

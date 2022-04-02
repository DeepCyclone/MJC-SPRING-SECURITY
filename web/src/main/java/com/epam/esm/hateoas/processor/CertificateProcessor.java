package com.epam.esm.hateoas.processor;


import com.epam.esm.controller.CertificateController;
import com.epam.esm.hateoas.model.CertificateModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateProcessor implements RepresentationModelProcessor<CertificateModel> {

    @Override
    public CertificateModel process(CertificateModel model) {
        return model.
        add(linkTo(methodOn(CertificateController.class).getByID(model.getId() + 1)).withRel("next"));
    }
    
}

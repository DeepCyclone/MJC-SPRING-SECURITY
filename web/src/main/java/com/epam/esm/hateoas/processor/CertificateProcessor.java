package com.epam.esm.hateoas.processor;


import com.epam.esm.controller.CertificateController;
import com.epam.esm.hateoas.model.CertificateModel;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateProcessor implements RepresentationModelProcessor<CertificateModel> {


    public CertificateModel process(Page<CertificateModel> page)
    {
        return null;
    }

    @Override
    public CertificateModel process(CertificateModel model) {
        return model;
    }
    
}

package com.epam.esm.hateoas.processor;


import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.hateoas.model.CertificateModel;

import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateProcessor implements RepresentationModelProcessor<CertificateModel> {


    public void process(CollectionModel<CertificateModel> models, Page<GiftCertificate> content,
                        int currentPage, int limit,
                        String certificateNamePart, String descriptionPart,
                        Set<String> tagsNames,String certificateNameSortOrder,String certificateCreationDateSortOrder){
        WebMvcLinkBuilder nextLink = linkTo(methodOn(CertificateController.class).getAllByRequestParams(certificateNamePart,descriptionPart,tagsNames,certificateNameSortOrder,certificateCreationDateSortOrder,content.hasNext() ? currentPage + 1 : 1, limit));
        WebMvcLinkBuilder prevLink = linkTo(methodOn(CertificateController.class).getAllByRequestParams(certificateNamePart,descriptionPart,tagsNames,certificateNameSortOrder,certificateCreationDateSortOrder,content.hasPrevious() && content.previousPageable().hasPrevious() ? currentPage - 1 : content.getTotalPages(), limit));
        models.add(prevLink.withRel("prev"));
        models.add(nextLink.withRel("next"));

    }

    @Override
    public CertificateModel process(CertificateModel model) {
        return model;
    }
    
}

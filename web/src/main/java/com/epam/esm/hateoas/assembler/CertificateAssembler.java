package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.hateoas.model.CertificateModel;
import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler extends RepresentationModelAssemblerSupport<GiftCertificate,CertificateModel>{
    
    public CertificateAssembler() {
        super(CertificateController.class,CertificateModel.class);
    }

    @Override
    public CertificateModel toModel(GiftCertificate entity) {
        CertificateModel model = createModelWithId(entity.getId(),entity);
        populateFields(entity, model);
        generateLinks(entity, model);
        return model;
    }

    
    @Override
    public CollectionModel<CertificateModel> toCollectionModel(Iterable<? extends GiftCertificate> entities) {
        List<CertificateModel> models = new LinkedList<>();
        entities.forEach(entity->{
            CertificateModel model = toModel(entity);
            models.add(model);
        });
        return CollectionModel.of(models);
    }

    private void populateFields(GiftCertificate source,CertificateModel destination){
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setDuration(source.getDuration());
        destination.setPrice(source.getPrice());
        destination.setCreateDate(source.getCreateDate());
        destination.setLastUpdateDate(source.getLastUpdateDate());
    }

    private void generateLinks(GiftCertificate source,CertificateModel destination){
        Optional.ofNullable(source.getAssociatedTags()).ifPresent(tags->
        tags.forEach(tag->
        destination.add(linkTo(methodOn(TagController.class).getByID(tag.getId())).withRel("tags"))));
    }

    
}

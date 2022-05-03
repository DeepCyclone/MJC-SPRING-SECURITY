package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.TagController;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.repository.model.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagAssembler extends RepresentationModelAssemblerSupport<Tag,TagModel>{

    public TagAssembler() {
        super(TagController.class, TagModel.class);
    }

    @Override
    public TagModel toModel(Tag entity) {
        TagModel model = createModelWithId(entity.getId(),entity);
        populateFields(entity, model);
        generateLinks(entity, model);
        return model;
    }

    @Override
    public CollectionModel<TagModel> toCollectionModel(Iterable<? extends Tag> entities) {
        List<TagModel> models = new LinkedList<>();
        entities.forEach(entity->{
            TagModel model = toModel(entity);
            models.add(model);
        });
        return CollectionModel.of(models);
    }
    

    private void populateFields(Tag source,TagModel destination){
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    private void generateLinks(Tag source,TagModel destination){
        destination.add(linkTo(methodOn(TagController.class).getAssociatedCertificates(source.getId())).withRel("associated certificates"));
    }
}

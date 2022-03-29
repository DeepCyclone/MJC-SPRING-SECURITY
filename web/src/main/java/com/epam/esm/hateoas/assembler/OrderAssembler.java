package com.epam.esm.hateoas.assembler;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.controller.OrderController;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.repository.model.Order;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler extends RepresentationModelAssemblerSupport<Order,OrderModel>{

    public OrderAssembler() {
        super(OrderController.class,OrderModel.class);
    }

    @Override
    public OrderModel toModel(Order entity) {
        OrderModel model = createModelWithId(entity.getId(), entity);
        populateFields(entity,model);
        generateLinks(entity, model);
        return model;
    }

    @Override
    public CollectionModel<OrderModel> toCollectionModel(Iterable<? extends Order> entities) {
        List<OrderModel> models = new LinkedList<>();
        entities.forEach(entity->{
            OrderModel model = toModel(entity);
            models.add(model);
        });
        return CollectionModel.of(models);
    }

    private void populateFields(Order source,OrderModel destination){
        destination.setId(source.getId());
        destination.setPrice(source.getPrice());
        destination.setPurchaseDate(new Date(source.getPurchaseDate().getTime()));
    }

    private void generateLinks(Order source,OrderModel destination){
        source.getCertificates().forEach(cert->destination.add(linkTo(methodOn(CertificateController.class).getByID(cert.getId())).withRel("certificates")));
        //TODO how to add notes with available operations;only text without refs due to same links; affordance? or another HAL features
    }
    
}

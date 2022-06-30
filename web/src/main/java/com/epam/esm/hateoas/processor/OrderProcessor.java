package com.epam.esm.hateoas.processor;

import com.epam.esm.controller.OrderController;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.repository.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessor implements RepresentationModelProcessor<OrderModel> {


    public void process(CollectionModel<OrderModel> models,Page<Order> content,int currentPage,int limit){
        models.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getAll(content.hasNext() ? currentPage + 1 : 1, limit)).withRel("next"));
        models.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).getAll(content.hasPrevious() && content.previousPageable().hasPrevious() ? currentPage - 1 : content.getTotalPages(), limit)).withRel("prev"));
    }

    @Override
    public OrderModel process(OrderModel model) {
        return model;
    }
    
}

package com.epam.esm.hateoas.impl;

import com.epam.esm.controller.OrderController;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.hateoas.LinksBuilder;

import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserControllerLinkBuilder implements LinksBuilder<UserResponseDto>{
    @Override
    public void buildLinks(UserResponseDto response) {
        response.add();
        response.getOrders().forEach(order->order.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withRel("order")));
    }
    
}

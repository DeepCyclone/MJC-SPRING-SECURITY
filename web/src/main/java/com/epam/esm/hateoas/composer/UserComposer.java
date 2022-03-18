package com.epam.esm.hateoas.composer;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.User;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class UserComposer {
    public static UserModel toModel(User user){
        UserModel model = UserModel.
        builder().
        id(user.getId()).
        name(user.getName()).
        build();
        model.add(linkTo(methodOn(UserController.class).getUserInfo(model.getId())).withSelfRel());
        user.getOrders().forEach(order->model.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withRel("order")));
        return model;
    } 
}

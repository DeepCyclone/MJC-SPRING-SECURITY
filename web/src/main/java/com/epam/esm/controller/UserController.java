package com.epam.esm.controller;

import java.util.List;

import javax.validation.constraints.Min;

import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.assembler.UserAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.template.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users",produces={MediaType.APPLICATION_JSON_VALUE})
@Validated
public class UserController {


    private final UserService userService;
    private final UserAssembler userAssembler;
    private final OrderAssembler orderAssembler;

    @Autowired
    public UserController(UserService userService,
                          UserAssembler userAssembler,
                          OrderAssembler orderAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping
    public CollectionModel<UserModel> getUsersInfo(@RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                                   @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit){
        List<User> users = userService.getAll(page,limit);
        return userAssembler.toCollectionModel(users);
    }

    @GetMapping(value="/{userId:\\d+}")
    public UserModel getUserInfo(@PathVariable long userId){
        User entity = userService.getById(userId);
        return userAssembler.toModel(entity);
    }

    @GetMapping(value="/{userId:\\d+}/orders")
    public CollectionModel<OrderModel> getAllOrders(@PathVariable long userId){
        List<Order> orderResponseDtos = userService.getById(userId).getOrders();
        return orderAssembler.toCollectionModel(orderResponseDtos);
    }

    @GetMapping(value="/most-used-tag-with-richest-orders")
    public Tag getMostWidelyUsedTagWithRichestOrder(){
        return userService.fetchMostUsedTagWithRichestOrders();
    }

}


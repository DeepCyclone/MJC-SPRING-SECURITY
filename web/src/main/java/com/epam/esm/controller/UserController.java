package com.epam.esm.controller;

import java.util.List;

import javax.validation.constraints.Min;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.hateoas.composer.UserComposer;
import com.epam.esm.hateoas.impl.OrderControllerLinkBuilder;
import com.epam.esm.hateoas.impl.UserControllerLinkBuilder;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.template.OrderService;
import com.epam.esm.service.template.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users",produces={MediaType.APPLICATION_JSON_VALUE})
public class UserController {


    private final UserService userService;
    private final OrderService OrderService;
    private final UserConverter userConverter;
    private final OrderConverter orderConverter;
    private final UserControllerLinkBuilder userLinkBuilder;
    private final OrderControllerLinkBuilder orderLinkBuilder;

    @Autowired
    public UserController(UserService userService,
                          OrderService OrderService,
                          UserConverter userConverter,
                          OrderConverter orderConverter,
                          UserControllerLinkBuilder userLinkBuilder,
                          OrderControllerLinkBuilder orderLinkBuilder) {
        this.userService = userService;
        this.OrderService = OrderService;
        this.userConverter = userConverter;
        this.orderConverter = orderConverter;
        this.userLinkBuilder = userLinkBuilder;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @GetMapping
    public List<UserResponseDto> getUsersInfo(@RequestParam(defaultValue = "1") @Min(1) long limit,
                                              @RequestParam(defaultValue = "0") @Min(0) long offset){
        List<UserResponseDto> responseDtos = userConverter.toResponseDtos(userService.getAll(limit,offset));
        responseDtos.forEach(userLinkBuilder::buildLinks);
        return responseDtos;
    }

    @GetMapping(value="/{userId:\\d+}")
    public UserModel getUserInfo(@PathVariable long userId){
        return UserComposer.toModel(userService.getById(userId));//TODO here
    }

    @GetMapping(value="/{userId:\\d+}/orders")
    public List<OrderResponseDto> getAllOrders(@PathVariable long userId){
        List<OrderResponseDto> orderResponseDtos = orderConverter.convertToResponseDtos(userService.getById(userId).getOrders());
        orderResponseDtos.forEach(orderLinkBuilder::buildLinks);
        return orderResponseDtos;
    }

    @GetMapping(value="/{userId:\\d+}/orders/{orderId:\\d+}")
    public OrderResponseDto getOrder(@PathVariable long userId,@PathVariable long orderId){
        OrderResponseDto response = orderConverter.convertToResponseDto(OrderService.getById(orderId));
        orderLinkBuilder.buildLinks(response);
        return response;
    }

    @GetMapping(value="/most-used-tag-with-richest-orders")
    public Tag getMostWidelyUsedTagWithRichestOrder(){
        return userService.fetchMostUsedTagWithRichestOrders();
    }

}


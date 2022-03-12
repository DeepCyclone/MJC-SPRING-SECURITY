package com.epam.esm.controller;

import java.util.List;

import com.epam.esm.converter.impl.OrderConverter;
import com.epam.esm.converter.impl.UserConverter;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.dto.response.UserResponseDto;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.template.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/users",produces={MediaType.APPLICATION_JSON_VALUE})
public class UserController {


    private final UserService userService;
    private final UserConverter userConverter;
    private final OrderConverter orderConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter,OrderConverter orderConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.orderConverter = orderConverter;
    }

    @GetMapping
    public List<UserResponseDto> getUsersInfo(){
        return userConverter.toResponseDtos(userService.getAll());
    }

    @GetMapping(value="/{userId:\\d+}")
    public UserResponseDto getUserInfo(@PathVariable long userId){
        return userConverter.toResponseDto(userService.getById(userId));
    }

    @GetMapping(value="/{userId:\\d+}/orders")
    public List<OrderResponseDto> getAllOrders(@PathVariable long userId){
        return orderConverter.convertToResponseDtos(userService.getById(userId).getOrders());
    }

    // @GetMapping(value="/{userId:\\d+}order/{orderId:\\d+}")
    // public void getOrder(@PathVariable long userId,@PathVariable long orderId){
    //     return orderConverter.convertToResponseDto(userService.getById(userId).getOrders())
    // }
    
    @GetMapping(value="/most-used-tag-with-richest-orders")
    public Tag getMostWidelyUsedTagWithRichestOrder(){
        return userService.fetchMostUsedTagWithRichestOrders();
    }

}


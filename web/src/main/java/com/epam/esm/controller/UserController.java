package com.epam.esm.controller;

import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.assembler.TagAssembler;
import com.epam.esm.hateoas.assembler.UserAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.template.OrderService;
import com.epam.esm.service.template.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users",produces={MediaType.APPLICATION_JSON_VALUE})
@Validated
public class UserController {


    private final UserService userService;
    private final OrderService orderService;
    private final UserAssembler userAssembler;
    private final OrderAssembler orderAssembler;
    private final TagAssembler tagAssembler;

    @Autowired
    public UserController(UserService userService,
                          UserAssembler userAssembler,
                          OrderAssembler orderAssembler,
                          OrderService orderService,
                          TagAssembler tagAssembler) {
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.orderAssembler = orderAssembler;
        this.orderService = orderService;
        this.tagAssembler = tagAssembler;
    }

    @Operation(summary =  "Take all available users by pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Users fetched successfully",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = UserModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid pagination params",
        content = @Content) 
    })
    @GetMapping
    public CollectionModel<UserModel> getUsersInfo(@Parameter(description = "page of result") @RequestParam(defaultValue = "1") @Min(value = 1,message = "page >=1 ") Integer page,
                                                   @Parameter(description = "records per page") @RequestParam(defaultValue = "10") @Min(value = 1,message = "limit >=1 ") Integer limit){
        List<User> users = userService.getAll(page,limit);
        return userAssembler.toCollectionModel(users);
    }

    @Operation(summary =  "Get certificate by ID with links to associated tags if there are present")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "User found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = UserModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "User not found",
            content =  @Content),
    })
    @GetMapping(value="/{userId:\\d+}")
    public UserModel getUserInfo(@PathVariable long userId){//
        User entity = userService.getById(userId);
        return userAssembler.toModel(entity);
    }

    @Operation(summary =  "Take all available orders of selected user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Orders of selected user fetched successfully",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = OrderModel.class))}),
        @ApiResponse(responseCode = "404" , description = "User not found with specified ID",
        content = @Content) 
    })
    @GetMapping(value="/{userId:\\d+}/orders")
    public CollectionModel<OrderModel> getAllOrders(@PathVariable long userId){
        List<Order> orderResponseDtos = userService.getById(userId).getOrders();
        return orderAssembler.toCollectionModel(orderResponseDtos);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Tag found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = TagModel.class))}),
        @ApiResponse(responseCode = "404" , description = "Tag not found",
            content =  @Content),
    })
    @GetMapping(value="/most-used-tag-with-richest-orders")
    public TagModel getMostWidelyUsedTagWithRichestOrder(){
        return tagAssembler.toModel(userService.fetchMostUsedTagWithRichestOrders());
    }

    @Operation(summary =  "create Tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201" , description = "Order performed successfully",
            content = @Content),
        @ApiResponse(responseCode = "409" , description = "Conflict when creating.Check params",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "User or one certificate from all not found with received ID(s)",
            content =  @Content),
        @ApiResponse(responseCode = "400" , description = "User not found with received ID",
        content =  @Content)
            
    })
    @PostMapping(value="/{userId:\\d+}")
    public ResponseEntity<OrderModel> makeOrderOnCertificates(@PathVariable long userId,@RequestParam(name="certificateId") List<Long> certificates){
        Order order = orderService.makeOrder(certificates,userId);
        OrderModel model = orderAssembler.toModel(order);
        return new ResponseEntity<>(model,HttpStatus.CREATED);
    }

}


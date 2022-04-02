package com.epam.esm.controller;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.PatchDTO;
import com.epam.esm.dto.request.OrderDto;
import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.repository.model.Order;
import com.epam.esm.service.template.OrderService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping(value = "/api/v1/orders",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class OrderController {

    private final OrderConverter orderConverter;
    private final OrderService orderService;
    private final OrderAssembler orderAssembler;

    public OrderController(OrderService orderService,OrderAssembler orderAssembler,OrderConverter orderConverter) {
        this.orderConverter = orderConverter;
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    @Operation(summary =  "Take all available orders by pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Orders fetched with specified params",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = OrderModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid pagination params",
        content = @Content) 
    })
    @GetMapping
    public CollectionModel<OrderModel> getAll(@Parameter(description = "page of result") @RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                              @Parameter(description = "records per page") @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit){
       List<Order> orders = orderService.getAll(page,limit);
       return orderAssembler.toCollectionModel(orders);
    }   

    @Operation(summary =  "Get order by ID with links to associated certificates if there are present")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Order found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = OrderModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Order not found",
            content =  @Content),
    })
    @GetMapping(value = "/{id:\\d+}")
    public OrderModel getById(@Parameter(description = "id of order to be searched") @PathVariable long id){//
        Order order = orderService.getById(id);
        return orderAssembler.toModel(order);
    }

    @Operation(summary =  "delete order by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" , description = "Order deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Order not found",
            content =  @Content),
        @ApiResponse(responseCode = "409" , description = "Order creation error",
        content =  @Content)
    })
    @DeleteMapping(value="/{id:\\d+}")
    public ResponseEntity<Void> deleteById(@Parameter(description = "id of order to be searched") @PathVariable long id){//
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary =  "update order's data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201" , description = "Order update successfully",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = OrderModel.class))}),
        @ApiResponse(responseCode = "409" , description = "Conflict when updating.Check params",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Order not found",
            content =  @Content),
        @ApiResponse(responseCode = "400" , description = "Bad patch params. Check schema of OrderDto",
            content =  @Content)
            
    })
    @PatchMapping(value="/{id:\\d+}")
    public ResponseEntity<OrderModel> updateById(@PathVariable long id,
                                                 @RequestBody @Validated(PatchDTO.class) OrderDto orderDto){//
        Order updatedOrder = orderService.update(orderConverter.convertFromRequestDto(orderDto), id);
        OrderModel model = orderAssembler.toModel(updatedOrder);
        return new ResponseEntity<>(model,HttpStatus.CREATED);
    }
}

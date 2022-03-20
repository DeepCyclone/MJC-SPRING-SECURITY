package com.epam.esm.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import javax.validation.constraints.Min;

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

@RestController
@RequestMapping(value = "/api/v1/orders",produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final OrderConverter orderConverter;
    private final OrderService orderService;
    private final OrderAssembler orderAssembler;

    public OrderController(OrderConverter orderConverter, OrderService orderService,OrderAssembler orderAssembler) {
        this.orderConverter = orderConverter;
        this.orderService = orderService;
        this.orderAssembler = orderAssembler;
    }

    @GetMapping
    public CollectionModel<OrderModel> getAll(@RequestParam(defaultValue = "1") @Min(1) long limit,
                                         @RequestParam(defaultValue = "0") @Min(0) long offset){
       List<Order> orders = orderService.getAll(limit,offset);
       return orderAssembler.toCollectionModel(orders);
    }   

    @GetMapping(value = "/{id:\\d+}")
    public OrderModel getById(@PathVariable long id){
        Order order = orderService.getById(id);
        return orderAssembler.toModel(order);
    }

    @DeleteMapping(value="/{id:\\d+}")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value="/{id:\\d+}")
    public ResponseEntity<OrderModel> updateById(@PathVariable long id,@RequestBody @Validated(PatchDTO.class) OrderDto orderDto){
        Order updatedOrder = orderService.update(orderConverter.convertFromRequestDto(orderDto), id);
        OrderModel model = orderAssembler.toModel(updatedOrder);
        return new ResponseEntity<>(model,HttpStatus.CREATED);
    }

    @PostMapping(value="/{userId:\\d+}")
    public ResponseEntity<OrderModel> makeOrderOnCertificates(@PathVariable long userId,@RequestParam(name="certificateId") List<Long> certificates){
        Order order = orderService.makeOrder(certificates,userId);
        OrderModel model = orderAssembler.toModel(order);
        return new ResponseEntity<>(model,HttpStatus.CREATED);
    }
}

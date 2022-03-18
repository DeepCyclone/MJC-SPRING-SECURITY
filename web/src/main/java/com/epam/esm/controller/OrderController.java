package com.epam.esm.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import javax.validation.constraints.Min;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.PatchDTO;
import com.epam.esm.dto.request.OrderDto;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.hateoas.impl.OrderControllerLinkBuilder;
import com.epam.esm.repository.model.Order;
import com.epam.esm.service.template.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping(value = "/api/v1/orders",produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final OrderConverter orderConverter;
    private final OrderService orderService;
    private final OrderControllerLinkBuilder orderLinkBuilder;

    public OrderController(OrderConverter orderConverter, OrderService orderService,OrderControllerLinkBuilder orderLinkBuilder) {
        this.orderConverter = orderConverter;
        this.orderService = orderService;
        this.orderLinkBuilder = orderLinkBuilder;
    }

    @GetMapping
    public List<OrderResponseDto> getAll(@RequestParam(defaultValue = "1") @Min(1) long limit,
                                         @RequestParam(defaultValue = "0") @Min(0) long offset){
       List<OrderResponseDto> orders = orderConverter.convertToResponseDtos(orderService.getAll(limit,offset));
       orders.forEach(orderLinkBuilder::buildLinks);
       return orders;
    }   

    @GetMapping(value = "/{id:\\d+}")
    public OrderResponseDto getById(@PathVariable long id){
        OrderResponseDto response = orderConverter.convertToResponseDto(orderService.getById(id));
        orderLinkBuilder.buildLinks(response);
        return response;
    }

    @DeleteMapping(value="/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id){
        orderService.delete(id);
    }

    @PatchMapping(value="/{id:\\d+}")
    public ResponseEntity<OrderResponseDto> updateById(@PathVariable long id,@RequestBody @Validated(PatchDTO.class) OrderDto orderDto){
        Order updatedOrder = orderService.update(orderConverter.convertFromRequestDto(orderDto), id);
        OrderResponseDto response = orderConverter.convertToResponseDto(updatedOrder);
        orderLinkBuilder.buildLinks(response);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @PostMapping(value="/{userId:\\d+}")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto makeOrderOnCertificates(@PathVariable long userId,@RequestParam(name="certificateId") List<Long> certificates){
        OrderResponseDto response =  orderConverter.convertToResponseDto(orderService.makeOrder(certificates,userId));
        orderLinkBuilder.buildLinks(response);
        return response;
    }
}

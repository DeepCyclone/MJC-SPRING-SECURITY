package com.epam.esm.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.epam.esm.converter.OrderConverter;
import com.epam.esm.dto.response.OrderResponseDto;
import com.epam.esm.service.template.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(value = "/api/v1/orders",produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrderController {

    private final OrderConverter orderConverter;
    private final OrderService orderService;

    public OrderController(OrderConverter orderConverter, OrderService orderService) {
        this.orderConverter = orderConverter;
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderResponseDto> getAll(){
       return orderConverter.convertToResponseDtos(orderService.getAll());
    }   

    @GetMapping(value = "/{id:\\d+}")
    public OrderResponseDto getById(@PathVariable long id){
        return orderConverter.convertToResponseDto(orderService.getById(id));
    }

    @DeleteMapping(value="/{id:\\d+}")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value="/{userId:\\d+}")
    public ResponseEntity<OrderResponseDto> makeOrderOnCertificates(@PathVariable long userId,@RequestParam(name="certificateId") List<Long> certificates){
        return new ResponseEntity<>(orderConverter.convertToResponseDto(orderService.makeOrder(certificates,userId)),HttpStatus.OK);
    }
}

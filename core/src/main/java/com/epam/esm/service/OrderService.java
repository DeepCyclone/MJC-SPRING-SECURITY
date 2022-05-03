package com.epam.esm.service;

import com.epam.esm.repository.model.Order;

import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Page<Order> getAll(int limit,int offset);
    Order getById(long orderId);
    Order update(Order orderPatch,long orderId);
    void delete(long orderId);
    Order makeOrder(List<Long> certificates,long userId);
}

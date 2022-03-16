package com.epam.esm.service.template;

import java.util.List;

import com.epam.esm.repository.model.Order;

public interface OrderService {
    List<Order> getAll(long limit,long offset);
    Order getById(long orderId);
    Order update(Order orderPatch,long orderId);
    void delete(long orderId);
    Order makeOrder(List<Long> certificates,long userId);
}

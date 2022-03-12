package com.epam.esm.service.template;

import java.util.List;

import com.epam.esm.repository.model.Order;

public interface OrderService {
    List<Order> getAll();
    Order getById(long orderId);
    boolean update(Order orderPatch,long orderId);
    boolean delete(long orderId);
    Order makeOrder(List<Long> certificates,long userId);
}

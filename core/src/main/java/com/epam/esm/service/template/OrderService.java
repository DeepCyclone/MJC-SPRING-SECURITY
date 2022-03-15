package com.epam.esm.service.template;

import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.model.Order;

public interface OrderService {
    List<Order> getAll(Optional<Long> limit,Optional<Long> offset);
    Order getById(long orderId);
    Order update(Order orderPatch,long orderId);
    void delete(long orderId);
    Order makeOrder(List<Long> certificates,long userId);
}

package com.epam.esm.repository;

import java.math.BigDecimal;
import java.util.Optional;

import com.epam.esm.repository.model.Order;

public interface OrderCustomRepository extends GenericRepository<Order>{
    Optional<Order> makeOrder(BigDecimal totalPrice);
    Order save(Order object);
    boolean deleteById(long id);
}

package com.epam.esm.repository;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/*
 * A specification how to interact with datasource which contains orders
 * @author Flexus
 * */
public interface OrderRepository extends GenericRepository<Order>,Identifiable<Order> {
    Optional<Order> makeOrder(BigDecimal totalPrice);
    List<GiftCertificate> fetchAssociatedCertificates(long orderId);
}

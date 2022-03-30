package com.epam.esm.repository.template;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends GenericRepository<Order>,Identifiable<Order> {
    Optional<Order> makeOrder(BigDecimal totalPrice);
    void linkAssociatedCertificates(List<GiftCertificate> certificates,long orderId);
    List<GiftCertificate> fetchAssociatedCertificates(long orderId);
    void detachAssociatedCertificates(long orderId);
}

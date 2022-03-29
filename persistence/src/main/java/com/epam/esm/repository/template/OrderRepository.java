package com.epam.esm.repository.template;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;

public interface OrderRepository extends GenericRepository<Order>,Identifiable<Order> {
    Optional<Order> makeOrder(BigDecimal totalPrice);
    void linkAssociatedCertificates(List<GiftCertificate> certificates,long orderId);
    List<GiftCertificate> fetchAssociatedCertificates(long orderId);
    boolean detachAssociatedCertificates(long orderId);
}

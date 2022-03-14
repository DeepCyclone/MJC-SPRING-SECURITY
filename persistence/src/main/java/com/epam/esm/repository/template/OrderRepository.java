package com.epam.esm.repository.template;

import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;

public interface OrderRepository extends GenericRepository<Order>,Identifiable<Order>,Nameable<Order> {
    Optional<Order> makeOrder(List<GiftCertificate> certificates);
    void linkAssociatedCertificates(List<GiftCertificate> certificates,long orderId);
    List<GiftCertificate> fetchAssociatedCertificates(long orderId);
}

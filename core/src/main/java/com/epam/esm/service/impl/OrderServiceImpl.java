package com.epam.esm.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.template.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,GiftCertificateRepository giftCertificateRepository,UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> getAll(Optional<Long> limit,Optional<Long> offset) {
        List<Order> orders =  orderRepository.readAll(limit,offset);
        orders.forEach(order -> order.setCertificates(fetchAssociatedCertificates(order.getId())));
        return orders;
    }

    @Override
    public Order getById(long orderId) {
        Order order = orderRepository.getByID(orderId).orElseThrow(
            () -> new ServiceException(ErrorCode.ORDER_NOT_FOUND,"Cannot fetch order with id = "+orderId));
        order.setCertificates(fetchAssociatedCertificates(orderId));
        return order;
    }

    @Override
    public boolean update(Order orderPatch, long orderId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean delete(long orderId) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Order makeOrder(List<Long> certificatesIds,long userId) {
        //TODO check if user exists
        List<GiftCertificate> certificatesEntities = new LinkedList<>();

        certificatesIds.forEach(certId->certificatesEntities.add(giftCertificateRepository.getByID(certId).orElseThrow(
                ()->new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"cert not found with ID = "+certId))));
        Order order = orderRepository.makeOrder(certificatesEntities).orElseThrow(
            ()->new ServiceException(ErrorCode.ORDER_CREATION_ERROR,"Cannot create order"));
        linkOrderToUser(order, userId);
        linkCertificatesToOrder(order.getId(), certificatesEntities);
        order.setCertificates(fetchAssociatedCertificates(order.getId()));
        return order;
    }

    private void linkOrderToUser(Order order,long userId){
        userRepository.linkAssociatedOrder(order.getId(),userId);
    }

    private void linkCertificatesToOrder(long orderId,List<GiftCertificate> certificates){
        orderRepository.linkAssociatedCertificates(certificates,orderId);
    }

    private List<GiftCertificate> fetchAssociatedCertificates(long orderId){
        List<GiftCertificate> certificates = orderRepository.fetchAssociatedCertificates(orderId);
        for(GiftCertificate cert:certificates){
            cert.setAssociatedTags(giftCertificateRepository.fetchAssociatedTags(cert.getId()));
        }
        return certificates;
    }

}

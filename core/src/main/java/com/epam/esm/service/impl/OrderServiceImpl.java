package com.epam.esm.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.template.OrderService;
import com.epam.esm.service.validation.SignValidator;

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
    public List<Order> getAll(long limit,long offset) {
        checkPaginationOptions(limit, offset);
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
    public Order update(Order orderPatch, long orderId) {
        if(!orderRepository.update(orderPatch, orderId)){

        }
        return getById(orderId);
    }

    @Override
    public void delete(long orderId) {
        orderRepository.deleteByID(orderId);
    }

    @Override
    public Order makeOrder(List<Long> certificatesIds,long userId) {
        //TODO check if user exists
        List<GiftCertificate> certificatesEntities = new LinkedList<>();

        certificatesIds.forEach(certId->certificatesEntities.add(giftCertificateRepository.getByID(certId).orElseThrow(
                ()->new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"cert not found with ID = "+certId))));

        Order order = orderRepository.makeOrder(certificatesEntities).orElseThrow(
            ()->new ServiceException(ErrorCode.ORDER_CREATION_ERROR,"Cannot create order"));

        userRepository.linkAssociatedOrder(order.getId(),userId);
        orderRepository.linkAssociatedCertificates(certificatesEntities,order.getId());

        order.setCertificates(fetchAssociatedCertificates(order.getId()));
        return order;
    }

    private List<GiftCertificate> fetchAssociatedCertificates(long orderId){
        List<GiftCertificate> certificates = orderRepository.fetchAssociatedCertificates(orderId);
        for(GiftCertificate cert:certificates){
            cert.setAssociatedTags(giftCertificateRepository.fetchAssociatedTags(cert.getId()));
        }
        return certificates;
    }

    private void checkPaginationOptions(long limit,long offset){
        if(!(SignValidator.isPositiveLong(limit) && SignValidator.isNonNegative(offset))){
            throw new ServiceException(ErrorCode.ORDER_BAD_REQUEST_PARAMS,"bad pagination params");
        }
    }

}

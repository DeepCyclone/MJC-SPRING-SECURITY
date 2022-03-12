package com.epam.esm.service.impl;

import java.util.LinkedList;
import java.util.List;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.service.template.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.readAll();
    }

    @Override
    public Order getById(long orderId) {
        return orderRepository.getByID(orderId).orElseThrow(
            ()->new ServiceException(ErrorCode.ORDER_NOT_FOUND,"Cannot fetch tag with id = "+orderId));
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
        for(Long certId:certificatesIds){
            certificatesEntities.add(giftCertificateRepository.getByID(certId).orElseThrow(
                ()->new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"cert not found with ID = "+certId)));
        }
        Order order = orderRepository.makeOrder(certificatesEntities).orElseThrow(
            ()->new ServiceException(ErrorCode.ORDER_CREATION_ERROR,"Cannot create order"));
        
        linkOrderToUser(order, userId);
        linkCertificatesToOrder(order.getId(), certificatesEntities);

    }

    private void linkOrderToUser(Order order,long userId){

    }

    private void linkCertificatesToOrder(long orderId,List<GiftCertificate> certificates){

    }

}

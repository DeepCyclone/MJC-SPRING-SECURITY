package com.epam.esm.service.impl;

import java.util.List;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.template.UserService;
import com.epam.esm.service.validation.SignValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<User> getAll(long limit,long offset) {
        checkPaginationOptions(limit, offset);
        List<User> users = userRepository.readAll(limit,offset);
        for(User user:users){
            List<Order> orders = userRepository.fetchAssociatedOrders(user.getId());
            orders.forEach(order->order.setCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
            user.setOrders(orders);
        }
        return users;
    }

    @Override
    public User getById(long id) {
        User user =  userRepository.getByID(id).
        orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND, "Cannot fetch user with ID = "+id));
        List<Order> orders = userRepository.fetchAssociatedOrders(id);
        orders.forEach(order->order.setCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
        user.setOrders(orders);
        return user;
    }

    @Override
    public User getByName(String userName) {
        User user =  userRepository.getByName(userName).
        orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND, "Cannot fetch user with name = "+userName));
        List<Order> orders = userRepository.fetchAssociatedOrders(user.getId());
        orders.forEach(order->order.setCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
        user.setOrders(orders);
        return user;
    }

    @Override
    public Tag fetchMostUsedTagWithRichestOrders() {
        return userRepository.fetchMostUsedTagWithRichestOrders().orElseThrow(
            ()->new ServiceException(ErrorCode.TAG_NOT_FOUND,"NO DATA IN DB"));
    }

    private void checkPaginationOptions(long limit,long offset){
        if(!(SignValidator.isPositiveLong(limit) && SignValidator.isNonNegative(offset))){
            throw new ServiceException(ErrorCode.ORDER_BAD_REQUEST_PARAMS,"bad pagination params");
        }
    }
    
}

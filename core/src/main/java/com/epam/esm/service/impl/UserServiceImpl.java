package com.epam.esm.service.impl;

import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<User> getAll(int page,int limit) {
        List<User> users = userRepository.readAll(page,limit);
        for(User user:users){
            List<Order> orders = userRepository.fetchAssociatedOrders(user.getId());
            orders.forEach(order->order.setAssociatedCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
            user.setOrders(orders);
        }
        return users;
    }

    @Override
    public User getById(long id) {
        User user =  userRepository.findByID(id).
        orElseThrow(()->new ServiceException(ServiceErrorCode.USER_NOT_FOUND, "Cannot fetch user with ID = "+id));
        List<Order> orders = userRepository.fetchAssociatedOrders(id);
        orders.forEach(order->order.setAssociatedCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
        user.setOrders(orders);
        return user;
    }

    @Override
    public User getByName(String userName) {
        User user =  userRepository.findByName(userName).
        orElseThrow(()->new ServiceException(ServiceErrorCode.USER_NOT_FOUND, "Cannot fetch user with name = "+userName));
        List<Order> orders = userRepository.fetchAssociatedOrders(user.getId());
        orders.forEach(order->order.setAssociatedCertificates(orderRepository.fetchAssociatedCertificates(order.getId())));
        user.setOrders(orders);
        return user;
    }

    @Override
    public Tag fetchMostUsedTagWithRichestOrders() {
        return userRepository.fetchMostUsedTagWithRichestOrders().orElseThrow(
            ()->new ServiceException(ServiceErrorCode.TAG_NOT_FOUND,"NO DATA IN DB"));
    }
    
}

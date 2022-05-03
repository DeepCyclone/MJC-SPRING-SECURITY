package com.epam.esm.service.impl;

import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<User> getAll(int page,int limit) {
        return userRepository.findAll(PageRequest.of(page,limit));

    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id).
        orElseThrow(()->new ServiceException(ServiceErrorCode.USER_NOT_FOUND, "Cannot fetch user with ID = "+id));
    }

    @Override
    public User getByName(String userName) {
        return userRepository.findByName(userName).
        orElseThrow(()->new ServiceException(ServiceErrorCode.USER_NOT_FOUND, "Cannot fetch user with name = "+userName));
    }

    @Override
    public Tag fetchMostUsedTagWithRichestOrders() {
        return userRepository.fetchMostUsedTagWithRichestOrders().orElseThrow(
            ()->new ServiceException(ServiceErrorCode.TAG_NOT_FOUND,"NO DATA IN DB"));
    }
    
}

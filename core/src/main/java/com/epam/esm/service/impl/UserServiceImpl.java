package com.epam.esm.service.impl;

import java.util.List;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.template.UserService;
import com.epam.esm.service.validation.SignValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{


    private final UserRepository userRepository;
    

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAll(long limit,long offset) {
        checkPaginationOptions(limit, offset);
        List<User> users = userRepository.readAll(limit,offset);
        for(User user:users){
            user.setOrders(userRepository.fetchAssociatedOrders(user.getId()));
        }
        return users;
    }

    @Override
    public User getById(long id) {
        User user =  userRepository.getByID(id).
        orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND, "Cannot fetch user with ID = "+id));
        user.setOrders(userRepository.fetchAssociatedOrders(id));
        return user;
    }

    @Override
    public User getByName(String userName) {
        User user =  userRepository.getByName(userName).
        orElseThrow(()->new ServiceException(ErrorCode.USER_NOT_FOUND, "Cannot fetch user with name = "+userName));
        user.setOrders(userRepository.fetchAssociatedOrders(user.getId()));
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

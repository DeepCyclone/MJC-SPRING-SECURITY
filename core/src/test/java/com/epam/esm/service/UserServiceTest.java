package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.template.OrderRepository;
import com.epam.esm.repository.template.UserRepository;
import com.epam.esm.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private static UserRepository userRepository;
    @Mock
    private static OrderRepository orderRepository;
 
    private static UserServiceImpl service = new UserServiceImpl(userRepository,orderRepository);
 
    private static final Optional<User> USER = Optional.of(User.builder().id(1).build());
    private static final Optional<User> NON_EXISTING_USER = Optional.empty();
    private static final List<User> USERS = new ArrayList<>();
    
 
 
 
    @BeforeEach
    void init(){
        service = new UserServiceImpl(userRepository,orderRepository);
    }
 
    @Test
    void getByIDExistingEntry(){
        Mockito.when(userRepository.findByID(1L)).thenReturn(USER);
        Assertions.assertEquals(service.getById(1L),USER.get());
    }
 
    @Test
    void getByIDNonExistingEntry(){
        Mockito.when(userRepository.findByID(9999L)).thenReturn(NON_EXISTING_USER);
        Assertions.assertThrows(ServiceException.class, ()->service.getById(9999L));
 
    }

    @Test
    void getByName(){
        Mockito.when(userRepository.findByName("NAME")).thenReturn(USER);
        Assertions.assertEquals(service.getByName("NAME"),USER.get());
    }
 
    @Test
    void getAll(){
        Mockito.when(userRepository.readAll(0,0)).thenReturn(USERS);
        Assertions.assertEquals(USERS,service.getAll(0,0));
    }
 
}

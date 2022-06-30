package com.epam.esm.service.impl;

import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

//@Service
public class SecurityUserDetailsService{
//
//    private final UserRepository userRepository;
//
//    @Autowired
//    public SecurityUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByName(username);
//        return new org.springframework.security.core.userdetails.User(user.get().getName(),user.get().getPassword(), Collections.emptyList());
//    }
}

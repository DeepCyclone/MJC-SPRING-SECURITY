package com.epam.esm.repository;

import com.epam.esm.repository.model.User;

import org.springframework.data.repository.PagingAndSortingRepository;


/*
 * A specification how to interact with datasource which contains users
 * @author Flexus
 * */
public interface UserRepository extends PagingAndSortingRepository<User,Long>,Nameable<User>,UserCustomRepository{

}

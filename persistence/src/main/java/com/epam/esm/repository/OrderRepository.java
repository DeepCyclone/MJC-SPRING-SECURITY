package com.epam.esm.repository;

import com.epam.esm.repository.model.Order;

import org.springframework.data.repository.PagingAndSortingRepository;

/*
 * A specification how to interact with datasource which contains orders
 * @author Flexus
 * */
public interface OrderRepository extends PagingAndSortingRepository<Order,Long>,OrderCustomRepository {

}

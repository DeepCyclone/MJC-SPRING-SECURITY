package com.epam.esm.repository.impl;

import com.epam.esm.repository.OrderCustomRepository;
import com.epam.esm.repository.model.Order;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public class OrderCustomRepositoryImpl implements OrderCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Order save(Order object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(Order object, long id) {
        return entityManager.
        createQuery("UPDATE Order o SET o.price = ?1 where o.id = ?2").
        setParameter(1, object.getPrice()).
        setParameter(2, id).
        executeUpdate() >= 1;
    }

    @Override
    public Optional<Order> makeOrder(BigDecimal totalPrice) {
        return Optional.ofNullable(entityManager.
        merge(Order.builder().
        id(0L).
        price(totalPrice).
        build()));
    }
    
    @Override
    public boolean deleteById(long id) {
        return entityManager.
        createQuery("delete from Order o where o.id = :id").
        setParameter("id", id).
        executeUpdate() >= 1;
    }
}

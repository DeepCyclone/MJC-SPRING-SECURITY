package com.epam.esm.repository.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.template.OrderRepository;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository{

    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public Order create(Order object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> readAll(int page,int limit) {
        return entityManager.
        createQuery("From Order",Order.class).
        setFirstResult((page-1)*limit).
        setMaxResults(limit).
        getResultList();
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
    public Optional<Order> findByID(long id) {
        return Optional.ofNullable(entityManager.find(Order.class, id));
    }

    @Override
    public boolean deleteByID(long id) {
        return entityManager.
        createQuery("delete from Order o where o.id = :id").
        setParameter("id", id).
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

    @Transactional
    @Override
    public void linkAssociatedCertificates(List<GiftCertificate> certificates, long orderId) {
        findByID(orderId).ifPresent(order->order.getCertificates().addAll(certificates));
    }

    @Override
    public List<GiftCertificate> fetchAssociatedCertificates(long orderId) {
        return findByID(orderId).map(order->order.getCertificates()).orElse(Collections.emptyList());
    }

    @Override
    public void detachAssociatedCertificates(long orderId) {
        findByID(orderId).ifPresent(order->order.getCertificates().clear());
    }

    @Override
    public boolean checkExistence(long id) {
        try{
            return entityManager.
            createQuery("SELECT 1 FROM Order order WHERE order.id = ?1",Integer.class).
            setParameter(1, id).
            getSingleResult() == 1;
        }
        catch(NoResultException ex){
            return false;
        }
    }



    
}

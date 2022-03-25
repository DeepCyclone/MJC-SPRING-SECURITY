package com.epam.esm.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
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
        // return Optional.ofNullable(
        // entityManager.
        // createQuery(OrderQueryHolder.CREATE_NEW_ENTRY,Order.class).
        // setParameter(1, totalPrice).
        // getSingleResult());


        // KeyHolder holder = new GeneratedKeyHolder();
        // jdbcTemplate.update(con->{
        //     PreparedStatement stmt = con.prepareStatement(OrderQueryHolder.CREATE_NEW_ENTRY,PreparedStatement.RETURN_GENERATED_KEYS);
        //     stmt.setBigDecimal(1, sum);
        //     return stmt;
        // },holder);
        // Optional<Order> order = Optional.empty();
        // if(holder.getKey()!=null){
        //     order = findByID(holder.getKey().longValue());
        // }
        // return order;
    }

    @Transactional
    @Override
    public void linkAssociatedCertificates(List<GiftCertificate> certificates, long orderId) {
        // certificates.forEach(cert->{
        //     // jdbcTemplate.update(OrderQueryHolder.INSERT_INTO_ORDER_M2M_CERT, orderId,cert.getId());//TODO can this method throw any exceptions
        // });
        findByID(orderId).ifPresent(order->order.getCertificates().addAll(certificates));
    }

    @Override
    public List<GiftCertificate> fetchAssociatedCertificates(long orderId) {
        return findByID(orderId).get().getCertificates();
    }

    @Override
    public boolean detachAssociatedCertificates(long orderId) {
        findByID(orderId).get().getCertificates().clear();
        return true;
    }

    @Override
    public boolean checkExistence(long id) {
        return true;
    }



    
}

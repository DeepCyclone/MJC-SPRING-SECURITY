package com.epam.esm.repository.impl;


import static com.epam.esm.repository.query.holder.UserQueryHolder.FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;



import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.template.UserRepository;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User create(User object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> readAll(int page,int limit) {
        return entityManager.
        createQuery("From User",User.class).
        setFirstResult((page-1)*limit).
        setMaxResults(limit).
        getResultList();
    }

    @Override
    public boolean update(User object, long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByID(long ID) {
        return Optional.ofNullable(entityManager.find(User.class, ID));
    }

    @Override
    public boolean deleteByID(long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> findByName(String name) {
        try {
            return Optional.ofNullable(
            entityManager.
            createQuery("SELECT FROM User user WHERE user.name =:name",User.class).
            setParameter("name",name).
            getSingleResult());
        }
        catch (NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Order> fetchAssociatedOrders(long userId) {
        return findByID(userId).map(user->user.getOrders()).orElse(Collections.emptyList());
    }

    @Override
    public Optional<Tag> fetchMostUsedTagWithRichestOrders() {
        try{
            return Optional.ofNullable(
            (Tag) entityManager.
            createNativeQuery(FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS,Tag.class).
            getSingleResult());
        }
        catch(NoResultException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean checkExistence(long id) {
        try{
            return entityManager.
            createQuery("SELECT 1 FROM User user WHERE user.id = ?1",Integer.class).
            setParameter(1, id).
            getSingleResult() == 1;
        }
        catch(NoResultException ex){
            return false;
        }
    }
    
}

package com.epam.esm.repository.impl;


import com.epam.esm.repository.UserCustomRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.holder.UserQueryHolder.FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS;

@Repository
public class UserCustomRepositoryImpl implements UserCustomRepository<User> {
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User object) {
        object.setId(0L);
        return entityManager.merge(object);
    }

    @Override
    public boolean update(User object, long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(long ID) {
        throw new UnsupportedOperationException();
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
    
}

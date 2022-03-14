package com.epam.esm.repository.impl;

import static com.epam.esm.repository.query.holder.UserQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.UserQueryHolder.READ_BY_ID;
import static com.epam.esm.repository.query.holder.UserQueryHolder.READ_BY_NAME;
import static com.epam.esm.repository.query.holder.UserQueryHolder.FETCH_ASSOCIATED_ORDERS;
import static com.epam.esm.repository.query.holder.UserQueryHolder.FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS;

import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.mapping.OrderMapping;
import com.epam.esm.repository.mapping.TagMapping;
import com.epam.esm.repository.mapping.UserMapping;

import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;
import com.epam.esm.repository.query.holder.UserQueryHolder;
import com.epam.esm.repository.template.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final UserMapping userMapper;
    private final OrderMapping orderMapper;
    private final TagMapping tagMapper;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate, UserMapping userMapper, OrderMapping orderMapper,TagMapping tagMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.tagMapper = tagMapper;
    }

    @Override
    public User create(User object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<User> readAll() {
        return jdbcTemplate.query(READ_ALL,userMapper);
    }

    @Override
    public boolean update(User object, long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> getByID(long ID) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(READ_BY_ID, userMapper, ID));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByID(long ID) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<User> getByName(String name) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(READ_BY_NAME, userMapper, name));
        }
        catch (DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Order> fetchAssociatedOrders(long userId) {
        return jdbcTemplate.query(FETCH_ASSOCIATED_ORDERS,orderMapper,userId);
    }

    @Override
    public Optional<Tag> fetchMostUsedTagWithRichestOrders() {
        try{
            return Optional.of(jdbcTemplate.queryForObject(FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS,tagMapper));
        }
        catch(DataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public boolean linkAssociatedOrder(long orderId, long userId) {
       return jdbcTemplate.update(UserQueryHolder.INSERT_INTO_M2M_USER_ORDERS, userId,orderId) >= 1;
    }


    
}

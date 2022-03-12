package com.epam.esm.repository.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.mapping.OrderMapping;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.query.holder.OrderQueryHolder;
import com.epam.esm.repository.template.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository{

    private final JdbcTemplate jdbcTemplate;
    private final OrderMapping orderMapper;

    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate, OrderMapping orderMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderMapper = orderMapper;
    }
    
    @Override
    public Order create(Order object) {
        return null;
    }

    @Override
    public List<Order> readAll() {
        return jdbcTemplate.query(OrderQueryHolder.READ_ALL, orderMapper);
    }

    @Override
    public boolean update(Order object, long ID) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Order> getByID(long id) {
        try{
            return Optional.of(jdbcTemplate.queryForObject(OrderQueryHolder.READ_BY_ID,orderMapper,id));
        }
        catch(DataAccessException ex){
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteByID(long ID) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Optional<Order> getByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<Order> makeOrder(List<GiftCertificate> certificates) {
        BigDecimal totalPrice = new BigDecimal(0);
        certificates.forEach(cert -> totalPrice.add(cert.getPrice()));
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con->{
            PreparedStatement stmt = con.prepareStatement(OrderQueryHolder.CREATE_NEW_ENTRY,PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setBigDecimal(1, totalPrice);
            return stmt;
        },holder);
        Optional<Order> order = Optional.empty();
        if(holder.getKey()!=null){
            order = getByID(holder.getKey().longValue());
        }
        return order;
    }
    
}

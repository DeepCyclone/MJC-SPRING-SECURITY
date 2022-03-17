package com.epam.esm.repository.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.epam.esm.repository.mapping.GiftCertificateMapping;
import com.epam.esm.repository.mapping.OrderMapping;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.query.holder.OrderQueryHolder;
import com.epam.esm.repository.query.processor.PaginationProcessor;
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
    private final GiftCertificateMapping certificateMapper;

    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate, OrderMapping orderMapper,GiftCertificateMapping certificateMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderMapper = orderMapper;
        this.certificateMapper = certificateMapper;
    }
    
    @Override
    public Order create(Order object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Order> readAll(long limit,long offset) {
        String query = OrderQueryHolder.READ_ALL + PaginationProcessor.appendQueryWithPagination(limit, offset);//TODO pass params as placeholders values
        return jdbcTemplate.query(query,orderMapper);
    }

    @Override
    public boolean update(Order object, long id) {
        return jdbcTemplate.update(OrderQueryHolder.UPDATE_ENTRY, object.getPrice(),id) >= 1;
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
    public boolean deleteByID(long id) {
        return jdbcTemplate.update(OrderQueryHolder.DELETE_ENTRY, id) >= 1;//TODO psfs 1
    }

    @Override
    public Optional<Order> makeOrder(List<GiftCertificate> certificates) {
        BigDecimal sum = certificates.stream().map(cert->cert.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(con->{
            PreparedStatement stmt = con.prepareStatement(OrderQueryHolder.CREATE_NEW_ENTRY,PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setBigDecimal(1, sum);
            return stmt;
        },holder);
        Optional<Order> order = Optional.empty();
        if(holder.getKey()!=null){
            order = getByID(holder.getKey().longValue());
        }
        return order;
    }

    @Override
    public void linkAssociatedCertificates(List<GiftCertificate> certificates, long orderId) {
        certificates.forEach(cert->{
            jdbcTemplate.update(OrderQueryHolder.INSERT_INTO_ORDER_M2M_CERT, orderId,cert.getId());//TODO can this method throw any exceptions
        });
    }

    @Override
    public List<GiftCertificate> fetchAssociatedCertificates(long orderId) {
        return jdbcTemplate.query(OrderQueryHolder.FETCH_ASSOCIATED_CERTIFICATES,certificateMapper,orderId);
    }

    @Override
    public boolean detachAssociatedCertificates(long orderId) {
        return jdbcTemplate.update(OrderQueryHolder.DETACH_ASSOCIATED_CERTIFICATES,orderId) >= 1;
    }

    @Override
    public boolean checkExistence(long id) {
        return jdbcTemplate.queryForObject(OrderQueryHolder.CHECK_EXISTENCE,Integer.class,id) == 1;
    }
    
}

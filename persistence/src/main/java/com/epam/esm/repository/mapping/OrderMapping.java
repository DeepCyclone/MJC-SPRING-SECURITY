package com.epam.esm.repository.mapping;

import com.epam.esm.repository.metadata.OrderMetadata;
import com.epam.esm.repository.model.Order;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.GregorianCalendar;

@Component
public class OrderMapping implements RowMapper<Order>{

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Order.
        builder().
        id(rs.getLong(OrderMetadata.ID)).
        purchaseDate(rs.getTimestamp(OrderMetadata.PURCHASE_DATE,new GregorianCalendar())).
        price(rs.getBigDecimal(OrderMetadata.PRICE)).
        build();
    }
    
}

package com.epam.esm.repository.query.holder;

public class OrderQueryHolder {
    public static final String READ_BY_ID = "SELECT * FROM order WHERE o_id = ?";
    public static final String READ_ALL = "SELECT * FROM order";
    public static final String CREATE_NEW_ENTRY = "INSERT INTO order(o_price) VALUES(?)";
    public static final String INSERT_INTO_ORDER_M2M_CERT = "INSERT INTO order_m2m_certificate VALUES(?,?)";
}

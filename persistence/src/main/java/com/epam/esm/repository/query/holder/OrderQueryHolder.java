package com.epam.esm.repository.query.holder;

public class OrderQueryHolder {
    public static final String READ_BY_ID = "SELECT * FROM `order` WHERE o_id = ?";
    public static final String READ_ALL = "SELECT * FROM `order`";
    public static final String CREATE_NEW_ENTRY = "INSERT INTO `order`(o_price) VALUES(?)";
    public static final String INSERT_INTO_ORDER_M2M_CERT = "INSERT INTO order_m2m_certificate(omc_o_id,omc_gc_id) VALUES(?,?)";
    public static final String FETCH_ASSOCIATED_CERTIFICATES = "SELECT * FROM gift_certificate WHERE gc_id IN (SELECT omc_gc_id FROM order_m2m_certificate WHERE omc_o_id = ?)";
}

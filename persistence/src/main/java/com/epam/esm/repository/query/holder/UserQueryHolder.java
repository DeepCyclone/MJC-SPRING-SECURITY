package com.epam.esm.repository.query.holder;

public class UserQueryHolder {
    public static final String READ_ALL = "SELECT * from user";
    public static final String READ_BY_ID = "SELECT * from user WHERE u_id = ?";
    public static final String READ_BY_NAME = "SELECT * from user WHERE u_name = ?";
    public static final String FETCH_ASSOCIATED_ORDERS = "SELECT * FROM `order` WHERE o_id IN (SELECT umo_o_id FROM user_m2m_order WHERE umo_u_id = ?)";
    public static final String FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS = "SELECT tag_id AS t_id,t_name FROM " + 
    "(SELECT user_id,TOTAL_ORDERS_PRICE,umo_o_id AS order_id,omc_gc_id AS cert_id,tmgc_t_id AS tag_id,COUNT(tmgc_t_id) AS tags FROM " +
    "(SELECT umo_u_id AS user_id,SUM(o_price) as TOTAL_ORDERS_PRICE FROM user_m2m_order " +
    "JOIN `order` ON o_id = umo_o_id GROUP BY user_id ORDER BY TOTAL_ORDERS_PRICE DESC LIMIT 1 ) AS der" + 
    "JOIN user_m2m_order ON user_id = umo_u_id JOIN order_m2m_certificate on omc_o_id = umo_o_id " + 
    "JOIN tag_m2m_gift_certificate on omc_gc_id = tmgc_gc_id GROUP BY tag_id ORDER BY tags DESC LIMIT 1 ) AS der1 " + 
    "JOIN tag on tag_id = t_id";
    public static final String INSERT_INTO_M2M_USER_ORDERS = "INSERT INTO user_m2m_order(umo_u_id,umo_o_id) VALUES(?,?)";
    public static final String CHECK_EXISTENCE = "SELECT 1 FROM user WHERE u_id = ?";
}

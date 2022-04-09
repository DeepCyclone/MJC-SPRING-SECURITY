package com.epam.esm.repository.query.holder;

public final class UserQueryHolder {

    private UserQueryHolder(){

    }

    public static final String FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS = "SELECT tag_id AS t_id,t_name FROM " +
    "(SELECT user_id,TOTAL_ORDERS_PRICE,umo_o_id AS order_id,omc_gc_id AS cert_id,tmgc_t_id AS tag_id,COUNT(tmgc_t_id) AS tags FROM " +
    "(SELECT umo_u_id AS user_id,SUM(o_price) as TOTAL_ORDERS_PRICE FROM user_m2m_order " +
    "JOIN user_order ON o_id = umo_o_id GROUP BY user_id ORDER BY TOTAL_ORDERS_PRICE DESC LIMIT 1 ) AS der " + 
    "JOIN user_m2m_order ON user_id = umo_u_id JOIN order_m2m_certificate on omc_o_id = umo_o_id " + 
    "JOIN tag_m2m_gift_certificate on omc_gc_id = tmgc_gc_id GROUP BY tag_id ORDER BY tags DESC LIMIT 1 ) AS der1 " + 
    "JOIN tag on tag_id = t_id";

}

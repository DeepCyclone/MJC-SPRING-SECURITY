package com.epam.esm.repository.query.holder;

public class UserQueryHolder {
    public static final String READ_ALL = "SELECT * from user";
    public static final String READ_BY_ID = "SELECT * from user WHERE u_id = ?";
    public static final String READ_BY_NAME = "SELECT * from user WHERE u_name = ?";
    public static final String FETCH_ASSOCIATED_ORDERS = "SELECT t_id,t_name FROM tag WHERE t_id IN (SELECT tmgc_t_id FROM tag_m2m_gift_certificate WHERE tmgc_gc_id = ?)";
    public static final String FETCH_MOST_USED_TAG_WITH_RICHEST_ORDERS = "";
}

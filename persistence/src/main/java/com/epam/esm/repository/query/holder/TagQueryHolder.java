package com.epam.esm.repository.query.holder;

public class TagQueryHolder {
    public static final String FETCH_ASSOCIATED_CERTIFICATES = "SELECT * FROM gift_certificate WHERE gc_id IN (SELECT tmgc_gc_id FROM tag_m2m_gift_certificate WHERE tmgc_t_id = ?)";
    public static final String READ_BY_ID = "SELECT * FROM tag WHERE t_id = ?";
    public static final String READ_ALL = "SELECT * FROM tag";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE t_id = ?";
    public static final String INSERT_INTO_WITH_ID = "INSERT IGNORE INTO tag VALUES(?,?)";
    public static final String INSERT_INTO_BY_NAME = "INSERT IGNORE INTO tag(t_name) VALUES(?)";
    public static final String GET_BY_NAME = "SELECT * FROM tag WHERE t_name = ?";
    public static final String CHECK_EXISTENCE = "SELECT 1 FROM tag WHERE t_id = ?";
}

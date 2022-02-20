package com.epam.esm.repository.query;

public class CertificateQueryHolder {
    public static final String READ_ALL = "SELECT * FROM gift_certificate";
    public static final String ID_FILTER = " WHERE gc_id = ?";
    public static final String READ_BY_ID = READ_ALL + ID_FILTER;
    public static final String DELETE_ENTRY = "DELETE FROM gift_certificate WHERE gc_id = ?";
    public static final String DETACH_ASSOCIATED_TAGS = "DELETE * FROM tag_m2m_gift_certificate WHERE tmgc_gc_id = ?";
    public static final String UPDATE_QUERY = "UPDATE gift_certificate SET gc_name=?, gc_description=?, gc_price=?, gc_duration=? WHERE gc_id=?";
    public static final String JOIN_PARAMS = "SELECT gc_id,gc_name,gc_description,gc_price,gc_duration,gc_create_date,gc_last_update_date FROM gift_certificate " +
            " LEFT JOIN (SELECT t_name,tmgc_gc_id,t_id FROM tag JOIN" +
            " `tag_m2m_gift_certificate` ON t_id = tmgc_t_id ) AS ix ON gc_id = ix.tmgc_gc_id";
    public static final String TAG_NAME_FILTER = "  t_name = ";
    public static final String CERTIFICATE_NAME_SEARCH = " gc_name like binary ";
    public static final String CERTIFICATE_DESCRIPTION_SEARCH = " gc_description like binary ";
    public static final String ORDER_BY = " ORDER BY ";
    public static final String WHERE = " WHERE ";
    public static final String AND = " AND ";
}
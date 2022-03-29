package com.epam.esm.repository.metadata;

/*
* Class holder of giftcertificate datasource internal fields names
* */
public class GiftCertificateMetadata {
    public static final String TABLE_NAME = "gift_certificate";
    public static final String DB_PREFIX = "gc_";
    public static final String ID = DB_PREFIX + "id";
    public static final String NAME = DB_PREFIX + "name";
    public static final String DESCRIPTION = DB_PREFIX + "description";
    public static final String PRICE = DB_PREFIX + "price";
    public static final String DURATION = DB_PREFIX + "duration";
    public static final String CREATE_DATE = DB_PREFIX + "create_date";
    public static final String LAST_UPDATE_DATE = DB_PREFIX + "last_update_date";
}

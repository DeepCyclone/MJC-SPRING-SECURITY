package com.epam.esm.repository.metadata;

public final class OrderMetadata {

    private OrderMetadata(){

    }

    public static final String TABLE_NAME = "user_order";
    public static final String DB_PREFIX = "o_";
    public static final String ID = DB_PREFIX + "id";
    public static final String PRICE = DB_PREFIX + "price";
    public static final String PURCHASE_DATE = DB_PREFIX + "purchase_date";
}

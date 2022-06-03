package com.epam.esm.repository.metadata;

public final class UserMetadata {

    private UserMetadata(){

    }

    public static final String TABLE_NAME = "user";
    public static final String DB_PREFIX = "u_";
    public static final String ID = DB_PREFIX + "id";
    public static final String NAME = DB_PREFIX + "name";
    public static final String PASSWORD = DB_PREFIX + "password";
}

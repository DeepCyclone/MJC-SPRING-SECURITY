package com.epam.esm.repository.metadata;

public final class RoleMetadata {

    private RoleMetadata(){

    }

    public static final String TABLE_NAME = "role";
    public static final String DB_PREFIX = "r_";
    public static final String ID = DB_PREFIX + "id";
    public static final String NAME = DB_PREFIX + "name";
}

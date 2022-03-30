package com.epam.esm.repository.metadata;

/*
 * Class holder of tag datasource internal fields names
 * */

public final class TagMetadata {

    private TagMetadata(){

    }

    public static final String TABLE_NAME = "tag";
    public static final String DB_PREFIX = "t_";
    public static final String ID = DB_PREFIX + "id";
    public static final String NAME = DB_PREFIX + "name";
}

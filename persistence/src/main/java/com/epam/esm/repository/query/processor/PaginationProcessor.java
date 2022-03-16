package com.epam.esm.repository.query.processor;

public class PaginationProcessor {
    public static String appendQueryWithPagination(long limit,long offset){
        return " LIMIT " + limit + " OFFSET " + offset;
    }
}

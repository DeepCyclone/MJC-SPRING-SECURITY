package com.epam.esm.repository.query.processor;

import java.util.Optional;

public class PaginationProcessor {
    public static String appendQueryWithPagination(Optional<Long> limit,Optional<Long> offset){
        StringBuilder paginationPart = new StringBuilder();//FIXME avoid creation empty 16 symbols
        limit.ifPresent(limVal->paginationPart.append(" LIMIT " + limVal));
        offset.ifPresent(offsetVal->{
            if(limit.isPresent()){
                paginationPart.append(" OFFSET " + offsetVal);
            }
        });
        return paginationPart.toString();
    }
}

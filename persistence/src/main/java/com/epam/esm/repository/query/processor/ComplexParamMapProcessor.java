package com.epam.esm.repository.query.processor;

import static com.epam.esm.repository.query.processor.SQLParts.*;

import static com.epam.esm.repository.query.holder.CertificateQueryHolder.AND;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.OR;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_DESCRIPTION_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_NAME_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.JOIN_PARAMS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.ORDER_BY;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.TAG_NAME_FILTER;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.WHERE;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;

import org.springframework.util.MultiValueMap;

public class ComplexParamMapProcessor {
    public static final String TAG_NAME = "tagName";
    public static final String NAME_PART = "namePart";
    public static final String DESCRIPTION_PART = "descriptionPart";
    public static final String NAME_SORT_ORDER = "nameSortOrder";
    public static final String DATE_SORT_ORDER = "dateSortOrder";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";


    public static String buildQuery(MultiValueMap<String,String> params){
        if(!params.isEmpty()) {
            StringBuilder query = new StringBuilder(JOIN_PARAMS);
            query.append(appendQueryWithSearching(params));
            query.append(appendQueryWithSorting(params));
            return query.toString();
        }
        return READ_ALL;
    }

    private static String appendQueryWithSearching(MultiValueMap<String,String> params){
        if(params.containsKey(TAG_NAME) || params.containsKey(NAME_PART) || params.containsKey(DESCRIPTION_PART)) {
            StringBuilder searchingQuery = new StringBuilder(WHERE);
            Optional<List<String>> tagsList = Optional.ofNullable(params.get(TAG_NAME));
            Optional<String> namePartValue = Optional.ofNullable(params.getFirst(NAME_PART));
            Optional<String> descriptionPartValue = Optional.ofNullable(params.getFirst(DESCRIPTION_PART));
            
            namePartValue.ifPresent(v -> {
                searchingQuery.append(CERTIFICATE_NAME_SEARCH).append(COLON).append(NAME_PART);
            });
            descriptionPartValue.ifPresent(v -> {
                if(namePartValue.isPresent()){searchingQuery.append(AND);}
                searchingQuery.append(CERTIFICATE_DESCRIPTION_SEARCH).append(COLON).append(DESCRIPTION_PART);
            });
            tagsList.ifPresent(list -> {
                Iterator<String> iterator = list.iterator();
                //TODO test case when empty list was passed
                    if(namePartValue.isPresent() || descriptionPartValue.isPresent()){
                        searchingQuery.append(AND);
                    }
                    int tagNum = 1;
                    while(iterator.hasNext()){
                    searchingQuery.append(TAG_NAME_FILTER).append(COLON).append(TAG_NAME+tagNum);
                    iterator.next();
                    if(iterator.hasNext()){
                        ++tagNum;
                        searchingQuery.append(OR);
                    }
                }
                searchingQuery.append(GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS+list.size());
            });
            return searchingQuery.toString();
        }
        return EMPTY_PART;
    }

    private static String appendQueryWithSorting(MultiValueMap<String,String> params){
        if((params.containsKey(NAME_SORT_ORDER)) || params.containsKey(DATE_SORT_ORDER)) {
            StringBuilder orderQuery = new StringBuilder(ORDER_BY);
            Optional<String> nameSortOrderValue = Optional.ofNullable(params.getFirst(NAME_SORT_ORDER));
            Optional<String> dateSortOrderValue = Optional.ofNullable(params.getFirst(DATE_SORT_ORDER));
            boolean complexSort = nameSortOrderValue.isPresent() && dateSortOrderValue.isPresent();
            nameSortOrderValue.ifPresent(order -> orderQuery.append(GiftCertificateMetadata.NAME).append(" ").append(order));
            //TODO arrange ORDER BY query according to params order in URL
            if(complexSort){
                orderQuery.append(COMMA);
            }
            dateSortOrderValue.ifPresent(order -> orderQuery.append(GiftCertificateMetadata.LAST_UPDATE_DATE).append(" ").append(order));
            return orderQuery.toString();
        }
        return EMPTY_PART;
    }
}

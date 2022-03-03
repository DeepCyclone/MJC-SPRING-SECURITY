package com.epam.esm.repository.query.processor;

import static com.epam.esm.repository.query.holder.CertificateQueryHolder.AND;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_DESCRIPTION_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_NAME_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.JOIN_PARAMS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.ORDER_BY;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.TAG_NAME_FILTER;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.WHERE;

import java.util.Map;
import java.util.Optional;

public class ComplexParamMapProcessor {
    public static final String tagName = "tagName";
    public static final String namePart = "namePart";
    public static final String descriptionPart = "descriptionPart";
    public static final String nameSortOrder = "nameSortOrder";
    public static final String dateSortOrder = "dateSortOrder";
    public static final String COLON = ":";
    public static final String PERCENT = "%";
    public static final String COMMA = ",";
    public static final String EMPTY_PART = "";


    public static String buildQuery(Map<String,String> params){
        if(!params.isEmpty()) {
            StringBuilder query = new StringBuilder(JOIN_PARAMS);
            query.append(appendQueryWithSearching(params));
            query.append(appendQueryWithSorting(params));
            return query.toString();
        }
        return READ_ALL;
    }

    private static String appendQueryWithSearching(Map<String,String> params){
        if(params.containsKey(tagName) || params.containsKey(namePart) || params.containsKey(descriptionPart)) {
            StringBuilder searchingQuery = new StringBuilder(WHERE);
            Optional<String> tagNameValue = Optional.ofNullable(params.get(tagName));
            Optional<String> namePartValue = Optional.ofNullable(params.get(namePart));
            Optional<String> descriptionPartValue = Optional.ofNullable(params.get(descriptionPart));
            tagNameValue.ifPresent(val -> searchingQuery.append(TAG_NAME_FILTER).append(COLON).append(tagName));
            namePartValue.ifPresent(val -> {
                if(tagNameValue.isPresent()){searchingQuery.append(AND);}
                searchingQuery.append(CERTIFICATE_NAME_SEARCH).append(COLON).append(namePart);
            });
            descriptionPartValue.ifPresent(val -> {
                if(tagNameValue.isPresent() || namePartValue.isPresent()){searchingQuery.append(AND);}
                searchingQuery.append(CERTIFICATE_DESCRIPTION_SEARCH).append(COLON).append(descriptionPart);
            });
            return searchingQuery.toString();
        }
        return EMPTY_PART;
    }

    private static String appendQueryWithSorting(Map<String,String> params){
        if((params.containsKey(nameSortOrder)) || params.containsKey(dateSortOrder)) {
            StringBuilder orderQuery = new StringBuilder(ORDER_BY);
            Optional<String> nameSortOrderValue = Optional.ofNullable(params.get(nameSortOrder));
            Optional<String> dateSortOrderValue = Optional.ofNullable(params.get(dateSortOrder));
            boolean complexSort = nameSortOrderValue.isPresent() && dateSortOrderValue.isPresent();
            nameSortOrderValue.ifPresent(order -> orderQuery.append("gc_name ").append(order));
            if(complexSort){
                orderQuery.append(COMMA);
            }
            dateSortOrderValue.ifPresent(order -> orderQuery.append("gc_last_update_date ").append(order));
            return orderQuery.toString();
        }
        return EMPTY_PART;
    }

}

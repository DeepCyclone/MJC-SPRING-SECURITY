package com.epam.esm.repository.query.processor;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.holder.CertificateQueryHolder.AND;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_DESCRIPTION_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_NAME_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.JOIN_PARAMS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.OR;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.ORDER_BY;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.READ_ALL;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.TAG_NAME_FILTER;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.WHERE;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DATE_SORT_ORDER;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DESCRIPTION_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_SORT_ORDER;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.TAG_NAME;
import static com.epam.esm.repository.query.holder.SQLParts.COLON;
import static com.epam.esm.repository.query.holder.SQLParts.COMMA;
import static com.epam.esm.repository.query.holder.SQLParts.EMPTY_PART;

public class ComplexParamMapProcessor {
 


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
                    if(namePartValue.isPresent() || descriptionPartValue.isPresent()){
                        searchingQuery.append(AND);
                    }
                    int tagNum = 1;
                    while(iterator.hasNext()){
                    searchingQuery.append(TAG_NAME_FILTER).append(COLON).append(TAG_NAME).append(tagNum);
                    iterator.next();
                    if(iterator.hasNext()){
                        ++tagNum;
                        searchingQuery.append(OR);
                    }
                }
                searchingQuery.append(GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS).append(list.size());
            });
            return searchingQuery.toString();
        }
        return EMPTY_PART;
    }

    private static String appendQueryWithSorting(MultiValueMap<String,String> params){
        Optional<String> nameSortOrderValue = Optional.ofNullable(params.getFirst(NAME_SORT_ORDER));
        Optional<String> dateSortOrderValue = Optional.ofNullable(params.getFirst(DATE_SORT_ORDER));
        if(nameSortOrderValue.isPresent() || dateSortOrderValue.isPresent()) {
            StringBuilder orderQuery = new StringBuilder(ORDER_BY);
            boolean complexSort = nameSortOrderValue.isPresent() && dateSortOrderValue.isPresent();
            nameSortOrderValue.ifPresent(order -> {
                orderQuery.append(GiftCertificateMetadata.NAME).append(" ").append(order);
            });
            if(complexSort){
                orderQuery.append(COMMA);
            }
            dateSortOrderValue.ifPresent(order -> {
                orderQuery.append(GiftCertificateMetadata.LAST_UPDATE_DATE).append(" ").append(order);
            });
            return orderQuery.toString();
        }
        return EMPTY_PART;
    }
    
}

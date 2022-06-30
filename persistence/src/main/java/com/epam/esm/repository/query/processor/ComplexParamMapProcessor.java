package com.epam.esm.repository.query.processor;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;

import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Set;

import static com.epam.esm.repository.query.holder.CertificateQueryHolder.AND;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_DESCRIPTION_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.CERTIFICATE_NAME_SEARCH;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.JOIN_PARAMS;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.OR;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.ORDER_BY;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.TAG_NAME_FILTER;
import static com.epam.esm.repository.query.holder.CertificateQueryHolder.WHERE;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DESCRIPTION_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.TAG_NAME;
import static com.epam.esm.repository.query.holder.SQLParts.COLON;
import static com.epam.esm.repository.query.holder.SQLParts.COMMA;
import static com.epam.esm.repository.query.holder.SQLParts.EMPTY_PART;
import static com.epam.esm.repository.query.holder.SQLParts.SPACE;

@Component
public class ComplexParamMapProcessor {

    public String buildQuery(String certificateNamePart, String descriptionPart,
                                    Set<String> tagsNames, String certificateNameSortOrder,
                                    String certificateCreationDateSortOrder){
        StringBuilder query = new StringBuilder(JOIN_PARAMS);
        query.append(appendQueryWithSearching(certificateNamePart,descriptionPart,tagsNames));
        query.append(appendQueryWithSorting(certificateNameSortOrder,certificateCreationDateSortOrder));
        return query.toString();
    }

    private String appendQueryWithSearching(String certificateNamePart,
                                                   String descriptionPart,
                                                   Set<String> tagsNames){
            StringBuilder searchingQuery = new StringBuilder(WHERE);
            
            searchingQuery.append(CERTIFICATE_NAME_SEARCH).append(COLON).append(NAME_PART);
            searchingQuery.append(AND);
            searchingQuery.append(CERTIFICATE_DESCRIPTION_SEARCH).append(COLON).append(DESCRIPTION_PART);

            if(tagsNames != null && tagsNames.size() > 0){
                Iterator<String> iterator = tagsNames.iterator();
                    searchingQuery.append(AND);
                    int tagNum = 1;
                    while(iterator.hasNext()){
                    searchingQuery.append(TAG_NAME_FILTER).append(COLON).append(TAG_NAME).append(tagNum);
                    iterator.next();
                    if(iterator.hasNext()){
                        ++tagNum;
                        searchingQuery.append(OR);
                    }
                }
                searchingQuery.append(GROUP_BY_CERT_WITH_CERTAIN_AMOUNT_OF_TAGS).append(tagsNames.size());
            };
            return searchingQuery.toString();
    }

    private String appendQueryWithSorting(String certificateNameSortOrder,String certificateCreationDateSortOrder){

        if(!certificateNameSortOrder.isEmpty() || !certificateCreationDateSortOrder.isEmpty()) {
            StringBuilder orderQuery = new StringBuilder(ORDER_BY);
            boolean complexSort = !certificateNameSortOrder.isEmpty() && !certificateCreationDateSortOrder.isEmpty();
            if(!certificateNameSortOrder.isEmpty()){
                orderQuery.append(GiftCertificateMetadata.NAME).append(SPACE).append(certificateNameSortOrder);
            }
            if(complexSort){
                orderQuery.append(COMMA);
            }
            if(!certificateCreationDateSortOrder.isEmpty()){
                orderQuery.append(GiftCertificateMetadata.CREATE_DATE).append(SPACE).append(certificateCreationDateSortOrder);
            }
            return orderQuery.toString();
        }
        return EMPTY_PART;
    }
    
}

package com.epam.esm.repository.query.processor;

import com.epam.esm.repository.model.GiftCertificate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import static com.epam.esm.repository.query.holder.SQLParts.COLON;
import static com.epam.esm.repository.query.holder.SQLParts.COMMA;
import static com.epam.esm.repository.query.holder.SQLParts.EMPTY_PART;
import static com.epam.esm.repository.query.holder.SQLParts.EQUALS_MARK;

public class UpdateQueryBuilder{
    private static final String UPDATE_QUERY_BASE = "UPDATE GiftCertificate certificate SET ";
    private static final String UPDATE_QUERY_WHERE_CONDITION = " WHERE certificate.id  = ";


    public static String buildUpdateQuery(GiftCertificate cert,long id){
        String dynamicPart = buildDynamicUpdatePart(cert);
        if(!dynamicPart.isEmpty()){
        StringBuilder resultQuery = new StringBuilder(UPDATE_QUERY_BASE);
        resultQuery.append(dynamicPart);
        resultQuery.append(UPDATE_QUERY_WHERE_CONDITION).append(id);
        return resultQuery.toString();
        }
        return EMPTY_PART;
    }

    public static Map<String,Object> getUpdateParams(GiftCertificate cert){
        Map<String,Object> params = new HashMap<>();
        Optional.ofNullable(cert.getName()).ifPresent(certName->params.put("name", certName));
        Optional.ofNullable(cert.getDescription()).ifPresent(certDescription->params.put("description", certDescription));
        Optional.ofNullable(cert.getPrice()).ifPresent(certPrice->params.put("price", certPrice));
        Optional.ofNullable(cert.getDuration()).ifPresent(certDuration->params.put("duration", certDuration));
        return params;
    }

    
    private static String buildDynamicUpdatePart(GiftCertificate cert){
        StringBuilder dynamicPart = new StringBuilder();
        Map<String,Object> params = getUpdateParams(cert);
        Iterator<String> keysIterator =  params.keySet().iterator();
        while(keysIterator.hasNext()){
            String key = keysIterator.next();
            dynamicPart.append(key).append(EQUALS_MARK).append(COLON).append(key);
            if(keysIterator.hasNext()){
                dynamicPart.append(COMMA);
            }
        }
        return dynamicPart.toString();
    }
}
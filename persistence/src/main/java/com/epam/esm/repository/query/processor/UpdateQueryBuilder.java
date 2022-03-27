package com.epam.esm.repository.query.processor;

import static com.epam.esm.repository.query.holder.SQLParts.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import com.epam.esm.repository.model.GiftCertificate;

public class UpdateQueryBuilder{
    private static final String UPDATE_QUERY_BASE = "UPDATE GiftCertificate certificate SET ";
    private static final String UPDATE_QUERY_WHERE_CONDITION = " WHERE certificate.id  = ";


    public static String buildUpdateQuery(GiftCertificate cert,long id){
        String dynamicPart = buildDynamicUpdatePart(cert);
        if(!dynamicPart.isEmpty()){
        StringBuilder resultQuery = new StringBuilder(UPDATE_QUERY_BASE);
        resultQuery.append(dynamicPart);
        resultQuery.append(UPDATE_QUERY_WHERE_CONDITION+id);
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
            dynamicPart.append(key+EQUALS_MARK+COLON+key);
            if(keysIterator.hasNext()){
                dynamicPart.append(COMMA);
            }
        }
        return dynamicPart.toString();
    }
}
package com.epam.esm.repository.query.processor;

import static com.epam.esm.repository.query.processor.SQLParts.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.model.GiftCertificate;

public class UpdateQueryBuilder{
    private static final String UPDATE_QUERY_BASE = "UPDATE " +  GiftCertificateMetadata.TABLE_NAME + " SET ";
    private static final String UPDATE_QUERY_WHERE_CONDITION = " WHERE " + GiftCertificateMetadata.ID + " = ";


    public static String buildUpdateQuery(GiftCertificate cert,long id){
        StringBuilder resultQuery = new StringBuilder(UPDATE_QUERY_BASE);
        resultQuery.append(buildDynamicUpdatePart(cert));
        resultQuery.append(UPDATE_QUERY_WHERE_CONDITION+id);//TODO can't mix ? and named param
        return resultQuery.toString();
    }

    public static Map<String,Object> getUpdateParams(GiftCertificate cert){
        Map<String,Object> params = new HashMap<>();
        Optional.ofNullable(cert.getName()).ifPresent(certName->params.put(GiftCertificateMetadata.NAME, certName));
        Optional.ofNullable(cert.getDescription()).ifPresent(certDescription->params.put(GiftCertificateMetadata.DESCRIPTION, certDescription));
        Optional.ofNullable(cert.getPrice()).ifPresent(certPrice->params.put(GiftCertificateMetadata.NAME, certPrice));
        Optional.ofNullable(cert.getDuration()).ifPresent(certDuration->params.put(GiftCertificateMetadata.NAME, certDuration));
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
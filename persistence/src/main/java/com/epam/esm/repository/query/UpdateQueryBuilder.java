package com.epam.esm.repository.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.epam.esm.repository.metadata.GiftCertificateMetadata;
import com.epam.esm.repository.model.GiftCertificate;

public class UpdateQueryBuilder{
    private static String UPDATE_QUERY_BASE = "UPDATE " +  GiftCertificateMetadata.TABLE_NAME + " SET ";
    private static String UPDATE_QUERY_WHERE_CONDITION = " WHERE " + GiftCertificateMetadata.ID + " = ";

    public static String buildUpdateQuery(GiftCertificate cert,long id){
        StringBuilder resultQuery = new StringBuilder(UPDATE_QUERY_BASE);
        resultQuery.append(buildDynamicUpdatePart(cert,id));
        resultQuery.append(UPDATE_QUERY_WHERE_CONDITION+id);
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

    
    private static String buildDynamicUpdatePart(GiftCertificate cert,long id){
        StringBuilder dynamicPart = new StringBuilder();
        Map<String,Object> params = getUpdateParams(cert);
        params.keySet().forEach(key -> dynamicPart.append(key).append("=").append(":"+key));
        return dynamicPart.toString();
    }
}
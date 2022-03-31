package com.epam.esm.repository.impl;

import com.epam.esm.repository.exception.RepositoryErrorCode;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.query.processor.ComplexParamMapProcessor;
import com.epam.esm.repository.query.processor.UpdateQueryBuilder;
import com.epam.esm.repository.template.GiftCertificateRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DATE_SORT_ORDER;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DESCRIPTION_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_SORT_ORDER;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.TAG_NAME;
import static com.epam.esm.repository.query.holder.SQLParts.PERCENT;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {


    public static final int MIN_AFFECTED_ROWS = 1;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public GiftCertificate create(GiftCertificate object){
        object.setId(0L);
        return this.entityManager.merge(object);
    }

    @Override
    public List<GiftCertificate> readAll(int page,int limit) {
        return entityManager.
        createQuery("From GiftCertificate",GiftCertificate.class).
        setFirstResult((page-1)*limit).
        setMaxResults(limit).
        getResultList();
    }

    @Override
    public boolean update(GiftCertificate object,long id) {
        String query = UpdateQueryBuilder.buildUpdateQuery(object, id);
        if(query.isEmpty()){
            return false;
        }
        Query queryExecutor = entityManager.
        createQuery(query);
        UpdateQueryBuilder.getUpdateParams(object).forEach(queryExecutor::setParameter);
        return queryExecutor.executeUpdate() >= MIN_AFFECTED_ROWS;
    }

    @Override
    public Optional<GiftCertificate> findByID(long id){
        return Optional.ofNullable(entityManager.
        find(GiftCertificate.class, id));
    }

    @Override
    public boolean deleteByID(long id) {
        findByID(id).ifPresent(cert -> {
            if(cert.getAssociatedOrders()!= null && !cert.getAssociatedOrders().isEmpty()){
                throw new RepositoryException(RepositoryErrorCode.CERTIFICATE_DELETION_ERROR, "Unable to delete certificate due to it's belonging to order(s)");
            }
        });
        return entityManager.
               createNativeQuery("delete from gift_certificate where gc_id = ?").
               setParameter(1, id).
               executeUpdate() >= MIN_AFFECTED_ROWS;
    }

    
    @Override
    public List<Tag> fetchAssociatedTags(long certificateID) {
        return this.findByID(certificateID).map(GiftCertificate::getAssociatedTags).orElse(Collections.emptyList());
    }
    
    @Override
    public List<GiftCertificate> handleParametrizedRequest(MultiValueMap<String,String> params,int page,int limit){
        String generatedQuery = ComplexParamMapProcessor.buildQuery(params);
        Query query = entityManager.
        createNativeQuery(generatedQuery,GiftCertificate.class).
        setFirstResult((page-1)*limit).
        setMaxResults(limit);
        prepareParamsToSearchStatement(params);
        prepareParamsToOrdering(params);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    public Optional<GiftCertificate> findByName(String name) {
        try{
            return Optional.ofNullable(entityManager.
            createQuery("SELECT FROM GiftCertificate certificate WHERE certificate.name = :name",GiftCertificate.class).
            setParameter("name", name).
            getSingleResult());
        }
        catch(NoResultException exc){
            return Optional.empty();
        }
    }

    private void prepareParamsToSearchStatement(MultiValueMap<String,String> params){
        if(params.containsKey(NAME_PART)){
            params.set(NAME_PART,PERCENT+params.getFirst(NAME_PART)+PERCENT);
        }
        if(params.containsKey(DESCRIPTION_PART)){
            params.set(DESCRIPTION_PART,PERCENT+params.getFirst(DESCRIPTION_PART)+PERCENT);
        }
        if(params.containsKey(TAG_NAME)){
        List<String> tags = params.get(TAG_NAME);
        int iter = 1;
        for(String tagName:tags){
            params.set(TAG_NAME+iter, tagName);
            iter++;
        }
        params.remove(TAG_NAME);
    }
    }

    private void prepareParamsToOrdering(MultiValueMap<String,String> params){
        params.remove(NAME_SORT_ORDER);
        params.remove(DATE_SORT_ORDER);
    }

    @Override
    public boolean checkExistence(long id) {
        try{
            return entityManager.
            createQuery("SELECT 1 FROM GiftCertificate certificate WHERE certificate.id = ?1",Integer.class).
            setParameter(1, id).
            getSingleResult() == 1;
        }
        catch(NoResultException ex){
            return false;
        }
    }


}

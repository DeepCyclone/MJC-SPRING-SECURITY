package com.epam.esm.repository.impl;

import com.epam.esm.repository.GiftCertificateCustomRepository;
import com.epam.esm.repository.exception.RepositoryErrorCode;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.query.processor.ComplexParamMapProcessor;
import com.epam.esm.repository.query.processor.UpdateQueryBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.repository.query.holder.ComplexParamsHolder.DESCRIPTION_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.NAME_PART;
import static com.epam.esm.repository.query.holder.ComplexParamsHolder.TAG_NAME;
import static com.epam.esm.repository.query.holder.SQLParts.PERCENT;

@Repository
public class GiftCertificateCustomRepositoryImpl implements GiftCertificateCustomRepository{


    public static final int MIN_AFFECTED_ROWS = 1;


    @PersistenceContext
    private EntityManager entityManager;
    private final UpdateQueryBuilder updateQueryBuilder;
    private final ComplexParamMapProcessor complexParamMapProcessor;

    @Autowired
    public GiftCertificateCustomRepositoryImpl(UpdateQueryBuilder updateQueryBuilder,ComplexParamMapProcessor complexParamMapProcessor) {
        this.complexParamMapProcessor = complexParamMapProcessor;
        this.updateQueryBuilder = updateQueryBuilder;
    }

    public boolean update(GiftCertificate object,long id) {
        String query = updateQueryBuilder.buildUpdateQuery(object, id);
        if(query.isEmpty()){
            return false;
        }
        Query queryExecutor = entityManager.
        createQuery(query);
        UpdateQueryBuilder.getUpdateParams(object).forEach(queryExecutor::setParameter);
        return queryExecutor.executeUpdate() >= MIN_AFFECTED_ROWS;
    }

    @Override
    public boolean deleteById(long id) {
        Optional.ofNullable(entityManager.find(GiftCertificate.class,id)).ifPresent(cert -> {
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
    public Page<GiftCertificate> handleParametrizedRequest(String certificateNamePart, String descriptionPart,
                                                           Set<String> tagsNames, String certificateNameSortOrder, String certificateCreationDateSortOrder, int page,
                                                           int limit) {
        String generatedQuery = complexParamMapProcessor.buildQuery(certificateNamePart,
                                                                    descriptionPart,
                                                                    tagsNames,
                                                                    certificateNameSortOrder,
                                                                    certificateCreationDateSortOrder
                                                                    );
        Query query = entityManager.
        createNativeQuery(generatedQuery,GiftCertificate.class).
        setParameter(NAME_PART, PERCENT+certificateNamePart+PERCENT).
        setParameter(DESCRIPTION_PART, PERCENT+descriptionPart+PERCENT).
        setFirstResult((page-1)*limit).
        setMaxResults(limit);
        setTagsNames(query,tagsNames);
        List<GiftCertificate> result = query.getResultList();
//        return new PageImpl<>(result, PageRequest.of(page, limit), result.size());
        return new PageImpl<>(result);

    }

    private void setTagsNames(Query query,Set<String> tagsNames){
        Optional.ofNullable(tagsNames).ifPresent(tags->{
            int iter = 1;
            for(String tagName:tags){
                query.setParameter(TAG_NAME+iter, tagName);
                iter++;
            }
        });

    }
}

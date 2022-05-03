package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagCustomRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryErrorCode;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class TagCustomRepositoryImpl implements TagCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public static final int MIN_AFFECTED_ROWS = 1;

    @Override
    public boolean update(Tag object,long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(long id) {
        Optional.ofNullable(entityManager.find(Tag.class,id)).ifPresent(tag->{
            if(!tag.getCerts().isEmpty()){
                throw new RepositoryException(RepositoryErrorCode.TAG_DELETION_ERROR,"cannot delete tag due to associated certificates");
            }
        });
        return entityManager.
        createQuery("DELETE FROM Tag tag WHERE tag.id = ?1").
        setParameter(1,id).
        executeUpdate() >= MIN_AFFECTED_ROWS;
    }

}

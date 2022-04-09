package com.epam.esm.repository.impl;

import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.RepositoryErrorCode;
import com.epam.esm.repository.exception.RepositoryException;
import com.epam.esm.repository.model.Tag;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
public class TagRepositoryImpl implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public static final int MIN_AFFECTED_ROWS = 1;

    @Override
    public Tag create(Tag object) {
       return entityManager.merge(object);
    }

    @Override
    public List<Tag> readAll(int page,int limit) {
        return entityManager.
        createQuery("From Tag",Tag.class).
        setFirstResult((page-1)*limit).
        setMaxResults(limit).
        getResultList();
    }

    @Override
    public boolean update(Tag object,long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Tag> findByID(long id) {
        return Optional.ofNullable(entityManager.find(Tag.class,id));
    }

    @Override
    public boolean deleteByID(long ID) {
        findByID(ID).ifPresent(tag->{
            if(!tag.getCerts().isEmpty()){
                throw new RepositoryException(RepositoryErrorCode.TAG_DELETION_ERROR,"cannot delete tag due to associated certificates");
            }
        });
        return entityManager.
        createQuery("DELETE FROM Tag tag WHERE tag.id = ?1").
        setParameter(1,ID).
        executeUpdate() >= MIN_AFFECTED_ROWS;
    }


    @Override
    public Optional<Tag> findByName(String name) {
        try{
            return Optional.ofNullable(entityManager.
            createQuery("Select tag From Tag tag where tag.name = :name",Tag.class).
            setParameter("name", name).
            getSingleResult());
        }
        catch(NoResultException exception){
            return Optional.empty();
        }
    }

    @Override
    public boolean checkExistence(long id) {
        try{
            return entityManager.
            createQuery("SELECT 1 FROM Tag tag WHERE tag.id = ?1",Integer.class).
            setParameter(1, id).
            getSingleResult() == 1;
        }
        catch(NoResultException ex){
            return false;
        }
    }


}

package com.epam.esm.repository;


import java.util.Optional;

/*
* An extension to {@link com.epam.esm.repository.GenericRepository} in case of object have ID field
* @author Flexus
* */
public interface Identifiable<T> {
    Optional<T> findByID(long ID);
    boolean deleteByID(long ID);
    boolean checkExistence(long id);
}

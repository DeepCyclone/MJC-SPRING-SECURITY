package com.epam.esm.repository.template;


import java.util.Optional;

/*
* An extension to {@link com.epam.esm.repository.GenericRepository} in case of object have ID field
* @author Flexus
* */
public interface Identifiable<T> {
    Optional<T> getByID(long ID);
    boolean deleteByID(long ID);
    boolean checkExistence(long id);
}

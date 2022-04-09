package com.epam.esm.repository;


import java.util.Optional;

/*
 * An extension to {@link com.epam.esm.repository.GenericRepository} in case of object have name field
 * @author Flexus
 * */
public interface Nameable<T> {
    Optional<T> findByName(String name);
}

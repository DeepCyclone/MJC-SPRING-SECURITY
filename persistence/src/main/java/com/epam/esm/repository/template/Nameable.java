package com.epam.esm.repository.template;


import java.util.Optional;

/*
 * An extension to {@link com.epam.esm.repository.GenericRepository} in case of object have name field
 * @author Flexus
 * */
public interface Nameable<T> {
    Optional<T> getByName(String name);
}

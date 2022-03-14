package com.epam.esm.repository.template;

import java.util.List;
import java.util.Optional;


/*
* Basic hierarchical interface,that describes available operations on objects
* @param X - typed object to perform some operations
* @author Flexus
* */
public interface GenericRepository <X>{
    X create(X object);
    List<X> readAll(Optional<Long> limit,Optional<Long> offset);
    boolean update(X object,long ID);
}

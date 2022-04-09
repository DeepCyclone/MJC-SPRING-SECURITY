package com.epam.esm.repository;

import java.util.List;


/*
* Basic hierarchical interface,that describes available operations on objects
* @param X - typed object to perform some operations
* @author Flexus
* */
public interface GenericRepository <X>{
    X create(X object);
    List<X> readAll(int page,int limit);
    boolean update(X object,long ID);
}

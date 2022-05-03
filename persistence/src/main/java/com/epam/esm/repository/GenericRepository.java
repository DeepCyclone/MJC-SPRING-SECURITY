package com.epam.esm.repository;



/*
* Basic hierarchical interface,that describes available operations on objects
* @param X - typed object to perform some operations
* @author Flexus
* */
public interface GenericRepository <X>{
    boolean update(X object,long ID);
}

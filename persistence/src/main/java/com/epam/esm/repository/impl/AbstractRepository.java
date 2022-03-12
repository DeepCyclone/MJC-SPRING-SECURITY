package com.epam.esm.repository.impl;

import java.util.List;

public abstract class AbstractRepository<T> {
    public List<T> readAll(){
        return null;
    }
    public T getById(long id){
        return null;
    }
    public T getByName(String name){
        return null;
    }
}

package com.epam.esm.repository;

import com.epam.esm.repository.model.Tag;

public interface TagCustomRepository extends GenericRepository<Tag> {
    boolean deleteById(long id);
}

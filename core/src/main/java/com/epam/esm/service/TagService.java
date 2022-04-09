package com.epam.esm.service;

import com.epam.esm.repository.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll(int limit,int offset);
    Tag getByID(long id);

    Tag addEntity(Tag tag);
    void deleteByID(long id);
}

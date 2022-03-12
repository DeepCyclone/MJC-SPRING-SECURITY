package com.epam.esm.service.template;

import com.epam.esm.repository.model.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getAll();
    Tag getByID(long id);
    Tag addEntity(Tag tag);
    void deleteByID(long id);
    void updateByID(long id) throws UnsupportedOperationException;
}

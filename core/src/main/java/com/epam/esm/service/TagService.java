package com.epam.esm.service;

import com.epam.esm.repository.model.Tag;

import org.springframework.data.domain.Page;

import java.util.Set;

public interface TagService {
    Page<Tag> getAll(int limit,int offset);
    Tag getByID(long id);
    Set<Tag> saveTags(Set<Tag> tags);
    Tag addEntity(Tag tag);
    void deleteByID(long id);
}

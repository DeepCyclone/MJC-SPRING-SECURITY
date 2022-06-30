package com.epam.esm.service.impl;


import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<Tag> getAll(int page,int limit){
        return tagRepository.findAll(PageRequest.of(page,limit));
    }
    @Override
    public Tag getByID(long id){
        return tagRepository.findById(id).orElseThrow(
                ()->new ServiceException(ServiceErrorCode.TAG_NOT_FOUND,"Couldn't fetch tag with id = "+id));
    }
    @Override
    @Transactional
    public Tag addEntity(Tag tag) {
        return tagRepository.findByName(tag.getName()).
        orElseGet(()->tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean flushingResult = tagRepository.deleteById(id);
        if(!flushingResult){
            throw new ServiceException(ServiceErrorCode.TAG_DELETION_ERROR,"Couldn't delete tag with id = "+id);
        }
    }

    @Override
    @Transactional
    public Set<Tag> saveTags(Set<Tag> tags){
        if(tags == null || tags.isEmpty()){
            return Collections.emptySet();
        }
        return tags.stream().map(this::addEntity).collect(Collectors.toSet());
    }

}

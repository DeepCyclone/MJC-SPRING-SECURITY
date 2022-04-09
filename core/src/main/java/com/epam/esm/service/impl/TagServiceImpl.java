package com.epam.esm.service.impl;


import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> getAll(int page,int limit){
        return tagRepository.readAll(page,limit);
    }
    @Override
    public Tag getByID(long id){
        return tagRepository.findByID(id).orElseThrow(
                ()->new ServiceException(ServiceErrorCode.TAG_NOT_FOUND,"Couldn't fetch tag with id = "+id));
    }
    @Override
    @Transactional
    public Tag addEntity(Tag tag) {
        return tagRepository.findByName(tag.getName()).
        orElseGet(()->tagRepository.create(tag));
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean flushingResult = tagRepository.deleteByID(id);
        if(!flushingResult){
            throw new ServiceException(ServiceErrorCode.TAG_DELETION_ERROR,"Couldn't delete tag with id = "+id);
        }
    }

}

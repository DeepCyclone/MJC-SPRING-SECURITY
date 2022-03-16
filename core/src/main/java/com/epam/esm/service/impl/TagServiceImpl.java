package com.epam.esm.service.impl;


import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.template.TagRepository;
import com.epam.esm.service.template.TagService;
import com.epam.esm.service.validation.SignValidator;

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
    public List<Tag> getAll(long limit,long offset){
        checkPaginationOptions(limit, offset);
        return tagRepository.readAll(limit,offset);
    }
    @Override
    public Tag getByID(long id){
        return tagRepository.getByID(id).orElseThrow(
                ()->new ServiceException(ErrorCode.TAG_NOT_FOUND,"Couldn't fetch tag with id = "+id));
    }
    @Override
    @Transactional
    public Tag addEntity(Tag tag) {
        return tagRepository.create(tag);
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean flushingResult = tagRepository.deleteByID(id);
        if(!flushingResult){
            throw new ServiceException(ErrorCode.TAG_DELETION_ERROR,"Couldn't delete tag with id = "+id);
        }
    }

    @Override
    public void updateByID(long id) throws UnsupportedOperationException {}

    private void checkPaginationOptions(long limit,long offset){
        if(!(SignValidator.isPositiveLong(limit) && SignValidator.isNonNegative(offset))){
            throw new ServiceException(ErrorCode.ORDER_BAD_REQUEST_PARAMS,"bad pagination params");
        }
    }

}

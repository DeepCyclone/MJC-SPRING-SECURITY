package com.epam.esm.service.impl;

import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validation.RequestParamsValidator;
import com.epam.esm.service.validation.UniqueValuesValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagService tagService) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
    }

    @Override
    //TODO transactional
    public GiftCertificate getByID(long id) {
        GiftCertificate certificate = certificateRepository.findByID(id).orElseThrow(
                ()->new ServiceException(ServiceErrorCode.CERTIFICATE_NOT_FOUND,"couldn't fetch certificate with id = "+ id));//TODO resource bundle
        certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(id));
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate addEntity(GiftCertificate certificateDto) {
        List<Tag> gainedTags = certificateDto.getAssociatedTags();
        certificateDto.setAssociatedTags(Collections.emptyList());
        GiftCertificate baseCert = certificateRepository.create(certificateDto);
        if(gainedTags!=null && !gainedTags.isEmpty()){
            List<Tag> uniqueTags = gainedTags.stream().
            filter(UniqueValuesValidator.distinctByKey(tag->tag.getName())).
            collect(Collectors.toList());
            List<Tag> savedTags = saveAssociatedTags(uniqueTags);
            baseCert.setAssociatedTags(new LinkedList<>());
            baseCert.getAssociatedTags().addAll(savedTags);
        }
        return getByID(baseCert.getId());//TODO get могут отрабатывать неправильно => разобраться с выполнением
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean result = certificateRepository.deleteByID(id);
        if(!result){
            throw new ServiceException(ServiceErrorCode.CERTIFICATE_DELETION_ERROR,"Cannot delete cert with id = " + id);
        }
    }

    @Override
    @Transactional
    public GiftCertificate update(GiftCertificate patch,long id) {
        checkExistence(id);
        certificateRepository.update(patch,id);
        Optional.ofNullable(patch.getAssociatedTags()).ifPresent(tags -> {
            GiftCertificate cert = getByID(id);
            List<Tag> uniqueTags = tags.stream().
            filter(UniqueValuesValidator.distinctByKey(tag->tag.getName())).
            collect(Collectors.toList());
            List<Tag> savedTags = saveAssociatedTags(uniqueTags);
            cert.getAssociatedTags().clear();
            cert.getAssociatedTags().addAll(savedTags);
        });
        return getByID(id);
    }

    //TODO перенести это в tagService
    @Override
    @Transactional
    public List<Tag> saveAssociatedTags(List<Tag> tags) {
        if(tags == null || tags.isEmpty()){
            return Collections.emptyList();
        }
        return tags.stream().map(tagService::addEntity).collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "certificatesCache",key = "new org.springframework.cache.interceptor.SimpleKey(#params.hashCode(), #page, #limit)")
    @Override
    public List<GiftCertificate> handleParametrizedGetRequest(MultiValueMap<String,String> params,int page,int limit){
        RequestParamsValidator.validateParams(params);
        List<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(params,page,limit);
        certificates.forEach(certificate -> certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(certificate.getId())));
        return certificates;
    }

    private void checkExistence(long id){
        if(!certificateRepository.checkExistence(id)){
            throw new ServiceException(ServiceErrorCode.CERTIFICATE_NOT_FOUND,"Cannot fetch certificate with ID " + id);
        }
    }

    @CacheEvict(cacheNames = "certificatesCache", allEntries = true)
    @Override
    public void clearCache() {
    }

}

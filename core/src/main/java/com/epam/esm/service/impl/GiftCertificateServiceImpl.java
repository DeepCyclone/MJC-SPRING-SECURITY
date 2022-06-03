package com.epam.esm.service.impl;

import com.epam.esm.exception.ServiceErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.validator.RequestParamsValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;




@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final RequestParamsValidator requestParamsValidator;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagService tagService,RequestParamsValidator requestParamsValidator) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.requestParamsValidator = requestParamsValidator;
    }

    @Override
    //TODO transactional
    public GiftCertificate getByID(long id) {
        GiftCertificate certificate = certificateRepository.findById(id).orElseThrow(
                ()->new ServiceException(ServiceErrorCode.CERTIFICATE_NOT_FOUND,"couldn't fetch certificate with id = "+ id));
        // certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(id));
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate addEntity(GiftCertificate certificateDto) {
        Set<Tag> gainedTags = certificateDto.getAssociatedTags();
        certificateDto.setAssociatedTags(Collections.emptySet());
        GiftCertificate baseCert = certificateRepository.save(certificateDto);
        if(gainedTags!=null && !gainedTags.isEmpty()){
            Set<Tag> savedTags = tagService.saveTags(gainedTags);
            baseCert.setAssociatedTags(new HashSet<>());
            baseCert.getAssociatedTags().addAll(savedTags);
        }
        return getByID(baseCert.getId());//TODO get могут отрабатывать неправильно => разобраться с выполнением
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean result = certificateRepository.deleteById(id);
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
            Set<Tag> savedTags = tagService.saveTags(tags);
            cert.getAssociatedTags().clear();
            cert.getAssociatedTags().addAll(savedTags);
        });
        return getByID(id);
    }

    @Cacheable(cacheNames = "certificatesCache",key = "new org.springframework.cache.interceptor.SimpleKey(#certificateNamePart,#descriptionPart,#tagsNames,#certificateNameSortOrder,#certificateCreationDateSortOrder, #page, #limit)")
    @Override
    public Page<GiftCertificate> handleParametrizedGetRequest(String certificateNamePart,
                                                              String descriptionPart,
                                                              Set<String> tagsNames,
                                                              String certificateNameSortOrder,
                                                              String certificateCreationDateSortOrder,
                                                              int page,
                                                              int limit){
        requestParamsValidator.validateSortingOrders(certificateNameSortOrder,certificateCreationDateSortOrder);
        Page<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(certificateNamePart,
                                                                                             descriptionPart,
                                                                                             tagsNames,
                                                                                             certificateNameSortOrder,
                                                                                             certificateCreationDateSortOrder,
                                                                                             page,
                                                                                             limit);
        return certificates;
    }

    private void checkExistence(long id){
        if(!certificateRepository.existsById(id)){
            throw new ServiceException(ServiceErrorCode.CERTIFICATE_NOT_FOUND,"Cannot fetch certificate with ID " + id);
        }
    }

    @CacheEvict(cacheNames = "certificatesCache", allEntries = true)
    @Override
    public void clearCache() {
    }

}

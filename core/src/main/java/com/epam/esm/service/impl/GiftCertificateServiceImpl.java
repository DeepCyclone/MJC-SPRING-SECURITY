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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;




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
        GiftCertificate certificate = certificateRepository.findByID(id).orElseThrow(
                ()->new ServiceException(ServiceErrorCode.CERTIFICATE_NOT_FOUND,"couldn't fetch certificate with id = "+ id));//TODO resource bundle
        certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(id));
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate addEntity(GiftCertificate certificateDto) {
        Set<Tag> gainedTags = certificateDto.getAssociatedTags();
        certificateDto.setAssociatedTags(Collections.emptySet());
        GiftCertificate baseCert = certificateRepository.create(certificateDto);
        if(gainedTags!=null && !gainedTags.isEmpty()){
            Set<Tag> savedTags = saveAssociatedTags(gainedTags);
            baseCert.setAssociatedTags(new HashSet<>());
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
            Set<Tag> savedTags = saveAssociatedTags(tags);
            cert.getAssociatedTags().clear();
            cert.getAssociatedTags().addAll(savedTags);
        });
        return getByID(id);
    }

    //TODO перенести это в tagService
    @Override
    @Transactional
    public Set<Tag> saveAssociatedTags(Set<Tag> tags) {
        if(tags == null || tags.isEmpty()){
            return Collections.emptySet();
        }
        return tags.stream().map(tagService::addEntity).collect(Collectors.toSet());
    }

    @Cacheable(cacheNames = "certificatesCache",key = "new org.springframework.cache.interceptor.SimpleKey(#certificateNamePart,#descriptionPart,#tagsNames,#certificateNameSortOrder,#certificateCreationDateSortOrder, #page, #limit)")
    @Override
    public List<GiftCertificate> handleParametrizedGetRequest(String certificateNamePart,
                                                              String descriptionPart,
                                                              Set<String> tagsNames,
                                                              String certificateNameSortOrder,
                                                              String certificateCreationDateSortOrder,
                                                              int page,
                                                              int limit){
        requestParamsValidator.validateSortingOrders(certificateNameSortOrder,certificateCreationDateSortOrder);
        List<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(certificateNamePart,
                                                                                             descriptionPart,
                                                                                             tagsNames,
                                                                                             certificateNameSortOrder,
                                                                                             certificateCreationDateSortOrder,
                                                                                             page,
                                                                                             limit);
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

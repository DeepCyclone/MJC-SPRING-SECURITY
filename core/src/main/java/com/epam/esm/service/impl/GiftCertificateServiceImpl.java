package com.epam.esm.service.impl;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.TagRepository;
import com.epam.esm.service.template.GiftCertificateService;
import com.epam.esm.service.validation.RequestParamsValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {



    

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public GiftCertificate getByID(long id) {
        GiftCertificate certificate = certificateRepository.findByID(id).orElseThrow(
                ()->new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"couldn't fetch certificate with id = "+ id));
        certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(id));
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate addEntity(GiftCertificate certificateDto) {
        List<Tag> gainedTags = certificateDto.getAssociatedTags();
        certificateDto.setAssociatedTags(Collections.emptyList());
        GiftCertificate baseCert = certificateRepository.create(certificateDto);
        List<Tag> savedTags = saveAssociatedTags(gainedTags);
        baseCert.getAssociatedTags().addAll(savedTags);
        return getByID(baseCert.getId());
    }

    @Override
    @Transactional
    public void deleteByID(long id){
        boolean result = certificateRepository.deleteByID(id);
        if(!result){
            throw new ServiceException(ErrorCode.CERTIFICATE_DELETION_ERROR,"Cannot delete cert with id = "+id);
        }
    }

    @Override
    public GiftCertificate update(GiftCertificate certificatePatch,long id) {
        // checkExistence(id);
        getByID(id);
        certificateRepository.update(certificatePatch,id);//TODO handle boolean value
        detachAssociatedTags(certificatePatch.getId());
        Optional.ofNullable(certificatePatch.getAssociatedTags()).ifPresent(tags -> {
            List<Tag> savedTags = saveAssociatedTags(tags);
            getByID(id).getAssociatedTags().addAll(savedTags);
            // certificateRepository.linkAssociatedTags(certificatePatch.getId(),savedTags);
        });
        return getByID(id);
    }

    @Override
    @Transactional
    public List<Tag> saveAssociatedTags(List<Tag> tags) {
        if(tags == null || tags.isEmpty()){
            return Collections.emptyList();
        }
        return tags.stream().map(tagRepository::create).collect(Collectors.toList());
    }

    @Override
    public List<GiftCertificate> handleParametrizedGetRequest(MultiValueMap<String,String> params,int page,int limit){
        RequestParamsValidator.validateParams(params);
        List<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(params,page,limit);
        certificates.forEach(certificate -> certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(certificate.getId())));
        return certificates;
    }
    
    private void detachAssociatedTags(long certificateID){
        certificateRepository.detachAssociatedTags(certificateID);
    }

    private void checkExistence(long id){
        if(!certificateRepository.checkExistence(id)){
            throw new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"Cannot fetch certificate with ID " + id);
        }
    }

}

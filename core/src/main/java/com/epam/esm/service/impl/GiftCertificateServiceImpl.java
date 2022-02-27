package com.epam.esm.service.impl;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.epam.esm.exception.ErrorCode.CERTIFICATE_BAD_REQUEST_PARAMS;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String NAME_SORT_ORDER = "nameSortOrder";
    private static final String DATE_SORT_ORDER = "dateSortOrder";
    private static final String ASCENDING_SORT = "ASC";
    private static final String DESCENDING_SORT = "DESC";

    private final GiftCertificateRepository certificateRepository;
    private final TagRepository tagRepository;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateRepository certificateRepository, TagRepository tagRepository) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public GiftCertificate getByID(long id) {
        GiftCertificate certificate = certificateRepository.getByID(id).orElseThrow(
                ()->new ServiceException(ErrorCode.CERTIFICATE_NOT_FOUND,"couldn't fetch certificate with id = "+ id));
        certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(id));
        return certificate;
    }

    @Override
    @Transactional
    public GiftCertificate addEntity(GiftCertificate certificateDto) {
        GiftCertificate baseCert = certificateRepository.create(certificateDto);
        List<Tag> savedTags = saveAssociatedTags(certificateDto.getAssociatedTags());
        certificateRepository.linkAssociatedTags(baseCert.getId(),savedTags);
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
    @Transactional
    public GiftCertificate update(GiftCertificate certificatePatch) {
        GiftCertificate originalCertificate = getByID(certificatePatch.getId());
        Optional.ofNullable(certificatePatch.getName()).ifPresent(originalCertificate::setName);
        Optional.ofNullable(certificatePatch.getDescription()).ifPresent(originalCertificate::setDescription);
        Optional.ofNullable(certificatePatch.getPrice()).ifPresent(originalCertificate::setPrice);
        originalCertificate.setDuration(certificatePatch.getDuration());
        detachAssociatedTags(certificatePatch.getId());
        Optional.ofNullable(certificatePatch.getAssociatedTags()).ifPresent(tags -> {
            List<Tag> savedTags = saveAssociatedTags(tags);
            certificateRepository.linkAssociatedTags(certificatePatch.getId(),savedTags);
        });
        certificateRepository.update(originalCertificate,certificatePatch.getId());
        return getByID(certificatePatch.getId());
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
    public List<GiftCertificate> handleParametrizedGetRequest(Map<String,String> params){
        checkInvalidValuedParams(params);
        List<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(params);
        certificates.forEach(certificate -> certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(certificate.getId())));
        return certificates;
    }

    private void checkInvalidValuedParams(Map<String,String> params){
        if((params.containsKey(NAME_SORT_ORDER) && !isAllowedOrderDirection(params.get(NAME_SORT_ORDER))) ||
                (params.containsKey(DATE_SORT_ORDER) && !isAllowedOrderDirection(params.get(DATE_SORT_ORDER)))){
            throw new ServiceException(CERTIFICATE_BAD_REQUEST_PARAMS,"allowed values for sorting params are ASC and DESC");
        }
    }

    private static boolean isAllowedOrderDirection(String order){
        return order.equalsIgnoreCase(ASCENDING_SORT) || order.equalsIgnoreCase(DESCENDING_SORT);
    }

    private void detachAssociatedTags(long certificateID){
        certificateRepository.detachAssociatedTags(certificateID);
    }

}

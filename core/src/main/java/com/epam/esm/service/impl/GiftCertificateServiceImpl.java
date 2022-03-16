package com.epam.esm.service.impl;

import com.epam.esm.exception.ErrorCode;
import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.repository.template.TagRepository;
import com.epam.esm.service.template.GiftCertificateService;
import com.epam.esm.service.validation.SignValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

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
    public GiftCertificate update(GiftCertificate certificatePatch,long id) {
        getByID(id);//TODO get atomic field or make boolean query in db
        certificateRepository.update(certificatePatch,id);
        detachAssociatedTags(certificatePatch.getId());
        Optional.ofNullable(certificatePatch.getAssociatedTags()).ifPresent(tags -> {
            List<Tag> savedTags = saveAssociatedTags(tags);
            certificateRepository.linkAssociatedTags(certificatePatch.getId(),savedTags);
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
    public List<GiftCertificate> handleParametrizedGetRequest(MultiValueMap<String,String> params,long limit,long offset){
        checkPaginationOptions(limit, offset);
        checkInvalidValuedParams(params);
        checkDuplicatedQueryParams(params);
        List<GiftCertificate> certificates = certificateRepository.handleParametrizedRequest(params,limit,offset);
        certificates.forEach(certificate -> certificate.setAssociatedTags(certificateRepository.fetchAssociatedTags(certificate.getId())));
        return certificates;
    }

    private void checkInvalidValuedParams(MultiValueMap<String,String> params){
        if((params.containsKey(NAME_SORT_ORDER) && !isAllowedOrderDirection(params.getFirst(NAME_SORT_ORDER))) ||
                (params.containsKey(DATE_SORT_ORDER) && !isAllowedOrderDirection(params.getFirst(DATE_SORT_ORDER)))){
            throw new ServiceException(CERTIFICATE_BAD_REQUEST_PARAMS,"allowed values for sorting params are ASC and DESC");
        }
    }

    private static boolean isAllowedOrderDirection(String order){
        return order.equalsIgnoreCase(ASCENDING_SORT) || order.equalsIgnoreCase(DESCENDING_SORT);
    }

    private void detachAssociatedTags(long certificateID){
        certificateRepository.detachAssociatedTags(certificateID);
    }

    private void checkDuplicatedQueryParams(MultiValueMap<String,String> params){
        List<Map.Entry<String,List<String>>> unallowedDuplicates = params.entrySet().stream().filter(entry -> 
        entry.getValue().size() > 1 && !entry.getKey().equals("tagName")).collect(Collectors.toList());
        if(!unallowedDuplicates.isEmpty()){
            StringBuilder duplicatesInfo = new StringBuilder();
            unallowedDuplicates.stream().forEach(entry-> duplicatesInfo.append(entry.getKey() + " can't have several values|"));
            throw new ServiceException(CERTIFICATE_BAD_REQUEST_PARAMS,duplicatesInfo.toString());
        }
    }

    private void checkPaginationOptions(long limit,long offset){
        if(!(SignValidator.isPositiveLong(limit) && SignValidator.isNonNegative(offset))){
            throw new ServiceException(ErrorCode.ORDER_BAD_REQUEST_PARAMS,"bad pagination params");
        }
    }
}

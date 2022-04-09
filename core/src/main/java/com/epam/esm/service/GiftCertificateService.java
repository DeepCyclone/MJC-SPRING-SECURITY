package com.epam.esm.service;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate getByID(long id);
    GiftCertificate addEntity(GiftCertificate certificateDto);
    void deleteByID(long id);
    GiftCertificate update(GiftCertificate certificatePatch,long id);
    List<Tag> saveAssociatedTags(List<Tag> tags);
    List<GiftCertificate> handleParametrizedGetRequest(MultiValueMap<String,String> params,int limit,int offset);
    void clearCache();
}

package com.epam.esm.service;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateService {
    GiftCertificate getByID(long ID);
    GiftCertificate addEntity(GiftCertificate certificateDto);
    void deleteByID(long ID);
    GiftCertificate update(GiftCertificate certificatePatch,long id);
    List<Tag> saveAssociatedTags(List<Tag> tags);
    List<GiftCertificate> handleParametrizedGetRequest(Map<String,String> params);
}

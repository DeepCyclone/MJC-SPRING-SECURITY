package com.epam.esm.service.template;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.util.MultiValueMap;

import java.util.List;

public interface GiftCertificateService {
    GiftCertificate getByID(long ID);
    GiftCertificate addEntity(GiftCertificate certificateDto);
    void deleteByID(long ID);
    GiftCertificate update(GiftCertificate certificatePatch,long id);
    List<Tag> saveAssociatedTags(List<Tag> tags);
    List<GiftCertificate> handleParametrizedGetRequest(MultiValueMap<String,String> params,int limit,int offset);
}

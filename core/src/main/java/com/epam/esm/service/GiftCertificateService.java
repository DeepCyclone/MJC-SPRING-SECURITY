package com.epam.esm.service;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface GiftCertificateService {
    GiftCertificate getByID(long id);
    GiftCertificate addEntity(GiftCertificate certificateDto);
    void deleteByID(long id);
    GiftCertificate update(GiftCertificate certificatePatch,long id);
    Page<GiftCertificate> handleParametrizedGetRequest(String certificateNamePart,
                                                       String descriptionPart,
                                                       Set<String> tagsNames,
                                                       String certificateNameSortOrder,
                                                       String certificateCreationDateSortOrder,
                                                       int page,
                                                       int limit);
    void clearCache();
}

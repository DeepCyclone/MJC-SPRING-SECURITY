package com.epam.esm.repository;

import java.util.List;
import java.util.Set;

import com.epam.esm.repository.model.GiftCertificate;

public interface GiftCertificateCustomRepository extends GenericRepository<GiftCertificate>{
    List<GiftCertificate> handleParametrizedRequest(String certificateNamePart,
                                                       String descriptionPart,
                                                       Set<String> tagsNames,
                                                       String certificateNameSortOrder,
                                                       String certificateCreationDateSortOrder,
                                                       int page,
                                                       int limit);
    boolean deleteById(long id);
}

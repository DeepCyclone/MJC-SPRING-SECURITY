package com.epam.esm.repository;

import java.util.Set;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.data.domain.Page;

public interface GiftCertificateCustomRepository extends GenericRepository<GiftCertificate>{
    Page<GiftCertificate> handleParametrizedRequest(String certificateNamePart,
                                                    String descriptionPart,
                                                    Set<String> tagsNames,
                                                    String certificateNameSortOrder,
                                                    String certificateCreationDateSortOrder,
                                                    int page,
                                                    int limit);
    boolean deleteById(long id);
}

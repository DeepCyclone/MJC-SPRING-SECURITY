package com.epam.esm.repository;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;

import java.util.List;
import java.util.Set;

/*
 * A specification how to interact with datasource which contains gift certificates
 * @author Flexus
 * */
public interface GiftCertificateRepository extends GenericRepository<GiftCertificate>,Identifiable<GiftCertificate>,Nameable<GiftCertificate> {
    Set<Tag> fetchAssociatedTags(long certificateID);
    List<GiftCertificate> handleParametrizedRequest(String certificateNamePart,
                                                       String descriptionPart,
                                                       Set<String> tagsNames,
                                                       String certificateNameSortOrder,
                                                       String certificateCreationDateSortOrder,
                                                       int page,
                                                       int limit);
}

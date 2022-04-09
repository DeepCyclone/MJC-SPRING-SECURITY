package com.epam.esm.repository;

import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import org.springframework.util.MultiValueMap;

import java.util.List;

/*
 * A specification how to interact with datasource which contains gift certificates
 * @author Flexus
 * */
public interface GiftCertificateRepository extends GenericRepository<GiftCertificate>,Identifiable<GiftCertificate>,Nameable<GiftCertificate> {
    List<Tag> fetchAssociatedTags(long certificateID);
    List<GiftCertificate> handleParametrizedRequest(MultiValueMap<String,String> map,int page,int limit);
}

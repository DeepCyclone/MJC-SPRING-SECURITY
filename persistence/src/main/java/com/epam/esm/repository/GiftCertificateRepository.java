package com.epam.esm.repository;

import com.epam.esm.dto.response.GiftCertificateResponseDto;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;

import java.util.List;
import java.util.Map;

public interface GiftCertificateRepository extends GenericRepository<GiftCertificate>,Identifiable<GiftCertificate>,Nameable<GiftCertificate> {
    void linkAssociatedTags(long certificateID, List<Tag> tags);
    boolean detachAssociatedTags(long certificateID);
    List<GiftCertificate> handleParametrizedRequest(Map<String,String> map);
}

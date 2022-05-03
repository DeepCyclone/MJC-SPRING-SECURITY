package com.epam.esm.repository;

import com.epam.esm.repository.model.GiftCertificate;
import org.springframework.data.repository.PagingAndSortingRepository;

/*
 * A specification how to interact with datasource which contains gift certificates
 * @author Flexus
 * */
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificate,Long>,Nameable<GiftCertificate>,GiftCertificateCustomRepository {

}

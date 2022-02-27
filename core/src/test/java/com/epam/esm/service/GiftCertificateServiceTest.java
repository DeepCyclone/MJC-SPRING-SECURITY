package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

   @Mock
   private static GiftCertificateRepository giftCertificateRepository;
   @Mock
   private static TagRepository tagRepository;

   private static GiftCertificateServiceImpl service = new GiftCertificateServiceImpl(giftCertificateRepository,tagRepository);

   private static final Optional<GiftCertificate> CERTIFICATE = Optional.of(GiftCertificate.builder().id(1).build());
   private static final Optional<GiftCertificate> NEW_CERT = Optional.of(GiftCertificate.builder().build());
   private static final Optional<GiftCertificate> NON_EXISTING_CERTIFICATE = Optional.empty();

   private static final List<GiftCertificate> certificates = Arrays.asList(new GiftCertificate());
   private static final List<GiftCertificate> parametrizedCertificates = Arrays.asList(GiftCertificate.builder().name("Aaaa").build());

   private static final List<Tag> tags = Collections.emptyList();
   private static final Tag emptyTag = Tag.builder().build();
   private static final Tag IDTag = Tag.builder().id(1L).build();


   @BeforeEach
   void init(){
       service = new GiftCertificateServiceImpl(giftCertificateRepository,tagRepository);
   }

   @Test
   void getByIDExistingEntry(){
       Mockito.when(giftCertificateRepository.getByID(1L)).thenReturn(CERTIFICATE);
       Assertions.assertEquals(service.getByID(1L),CERTIFICATE.get());

   }

   @Test
   void getByIDNonExistingEntry(){
       Mockito.when(giftCertificateRepository.getByID(9999L)).thenReturn(NON_EXISTING_CERTIFICATE);
       Assertions.assertThrows(ServiceException.class, ()->service.getByID(9999L));

   }

   @Test
   void getAllWithoutParams(){
       Mockito.when(giftCertificateRepository.handleParametrizedRequest(new HashMap<>())).thenReturn(certificates);
       Assertions.assertEquals(certificates,service.handleParametrizedGetRequest(new HashMap<>()));
   }

   @Test
   void getWithNamePart(){
       Map<String,String> params = new HashMap<>();
       params.put("namePart","A");
       Mockito.when(giftCertificateRepository.handleParametrizedRequest(params)).thenReturn(parametrizedCertificates);
       Assertions.assertEquals(parametrizedCertificates,service.handleParametrizedGetRequest(params));
   }

   @Test
   void deleteNonExistingEntry(){
       Mockito.when(giftCertificateRepository.deleteByID(25L)).thenReturn(false);
       Assertions.assertThrows(ServiceException.class,()->service.deleteByID(25L));
   }

   @Test
   void deleteEntry(){
       Mockito.when(giftCertificateRepository.deleteByID(1L)).thenReturn(true);
       Assertions.assertDoesNotThrow(()->service.deleteByID(1L));
   }

   @Test
   void addEntity(){
       Mockito.when(giftCertificateRepository.create(NEW_CERT.get())).thenReturn(CERTIFICATE.get());
       Mockito.when(giftCertificateRepository.getByID(Mockito.eq(1L))).thenReturn(CERTIFICATE);
       GiftCertificate entity = service.addEntity(NEW_CERT.get());
       Assertions.assertEquals(CERTIFICATE.get(),entity);
   }

   @Test
   void updateEntity(){
       Mockito.when(giftCertificateRepository.update(CERTIFICATE.get(),1L)).thenReturn(true);
       Mockito.when(giftCertificateRepository.getByID(Mockito.eq(1L))).thenReturn(CERTIFICATE);
       Assertions.assertEquals(CERTIFICATE.get(),service.update(CERTIFICATE.get()));
   }

   @Test
   void updateNonExistingEntity(){
       Mockito.when(giftCertificateRepository.getByID(Mockito.eq(1L))).thenReturn(NON_EXISTING_CERTIFICATE);
       Assertions.assertThrows(ServiceException.class,() -> service.update(CERTIFICATE.get()));
   }
}

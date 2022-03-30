package com.epam.esm.service;

import com.epam.esm.exception.ServiceException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.template.GiftCertificateRepository;
import com.epam.esm.service.impl.GiftCertificateServiceImpl;
import com.epam.esm.service.template.TagService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GiftCertificateServiceTest {

   @Mock
   private static GiftCertificateRepository giftCertificateRepository;
   @Mock
   private static TagService tagService;

   private static GiftCertificateServiceImpl service = new GiftCertificateServiceImpl(giftCertificateRepository,tagService);

   private static final Optional<GiftCertificate> CERTIFICATE = Optional.of(GiftCertificate.builder().id(1).build());
   private static final Optional<GiftCertificate> NEW_CERT = Optional.of(GiftCertificate.builder().build());
   private static final Optional<GiftCertificate> NON_EXISTING_CERTIFICATE = Optional.empty();

   private static final List<GiftCertificate> certificates = Arrays.asList(new GiftCertificate());
   private static final List<GiftCertificate> parametrizedCertificates = Arrays.asList(GiftCertificate.builder().name("Aaaa").build());


   @BeforeEach
   void init(){
       service = new GiftCertificateServiceImpl(giftCertificateRepository,tagService);
   }

   @Test
   void getByIDExistingEntry(){
       Mockito.when(giftCertificateRepository.findByID(1L)).thenReturn(CERTIFICATE);
       Assertions.assertEquals(service.getByID(1L),CERTIFICATE.get());

   }

   @Test
   void getByIDNonExistingEntry(){
       Mockito.when(giftCertificateRepository.findByID(9999L)).thenReturn(NON_EXISTING_CERTIFICATE);
       Assertions.assertThrows(ServiceException.class, ()->service.getByID(9999L));

   }

   @Test
   void getAllWithoutParams(){
       Mockito.when(giftCertificateRepository.handleParametrizedRequest(new LinkedMultiValueMap<>(),0,0)).thenReturn(certificates);
       Assertions.assertEquals(certificates,service.handleParametrizedGetRequest(new LinkedMultiValueMap<>(),0,0));
   }

   @Test
   void getWithNamePart(){
       MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
       params.add("namePart","A");
       Mockito.when(giftCertificateRepository.handleParametrizedRequest(params,0,0)).thenReturn(parametrizedCertificates);
       Assertions.assertEquals(parametrizedCertificates,service.handleParametrizedGetRequest(params,0,0));
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
       Mockito.when(giftCertificateRepository.findByID(Mockito.eq(1L))).thenReturn(CERTIFICATE);
       GiftCertificate entity = service.addEntity(NEW_CERT.get());
       Assertions.assertEquals(CERTIFICATE.get(),entity);
   }

   @Test
   void updateEntity(){
       Mockito.when(giftCertificateRepository.update(CERTIFICATE.get(),1L)).thenReturn(true);
       Mockito.when(giftCertificateRepository.findByID(Mockito.eq(1L))).thenReturn(CERTIFICATE);
       Mockito.when(giftCertificateRepository.checkExistence(Mockito.eq(1L))).thenReturn(true);
       Assertions.assertEquals(CERTIFICATE.get(),service.update(CERTIFICATE.get(),1L));
   }

   @Test
   void updateNonExistingEntity(){
       Mockito.when(giftCertificateRepository.checkExistence(Mockito.eq(1L))).thenReturn(false);
       Assertions.assertThrows(ServiceException.class,() -> service.update(CERTIFICATE.get(),1L));
   }
}

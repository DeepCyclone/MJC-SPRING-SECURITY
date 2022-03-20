package com.epam.esm.controller;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.PatchDTO;
import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.hateoas.assembler.CertificateAssembler;
import com.epam.esm.hateoas.model.CertificateModel;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.template.GiftCertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1/certificates",produces = {MediaType.APPLICATION_JSON_VALUE})
public class GiftCertificateController {

    private final GiftCertificateService certificateService;
    private final CertificateConverter certificateConverter;
    private final CertificateAssembler certificateAssembler;

    @Autowired
    public GiftCertificateController(GiftCertificateService certificateService, CertificateConverter certificateConverter,CertificateAssembler certificateAssembler) {
        this.certificateService = certificateService;
        this.certificateConverter = certificateConverter;
        this.certificateAssembler = certificateAssembler;
    }

    @GetMapping
    public CollectionModel<CertificateModel> getAllByRequestParams(@RequestParam MultiValueMap<String,String> params,
                                                                  @RequestParam(defaultValue = "1") @Min(1) long limit,
                                                                  @RequestParam(defaultValue = "0") @Min(0) long offset) {
        List<GiftCertificate> certs = certificateService.handleParametrizedGetRequest(params,limit,offset);
        return certificateAssembler.toCollectionModel(certs);
    }

    @GetMapping(value = "/{id:\\d+}")
    public CertificateModel getByID(@PathVariable long id) {
        return certificateAssembler.toModel(certificateService.getByID(id));
    }

    @DeleteMapping(value = "/{id:\\d+}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable long id){
        certificateService.deleteByID(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{id:\\d+}")
    public ResponseEntity<CertificateModel> updateCertificate(@RequestBody @Validated(PatchDTO.class) GiftCertificateDto certificateDtoPatch,@PathVariable long id){
        GiftCertificate certificate = certificateConverter.convertFromRequestDto(certificateDtoPatch);
        return new ResponseEntity<>(certificateAssembler.toModel(certificate),HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> createCertificate(@RequestBody @Validated(CreateDTO.class) GiftCertificateDto certificateDto) {
        GiftCertificate certificate = certificateConverter.convertFromRequestDto(certificateDto);
        return new ResponseEntity<>(certificateAssembler.toModel(certificate),HttpStatus.CREATED);
    }

}

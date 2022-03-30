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

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/certificates",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CertificateController {

    private final GiftCertificateService certificateService;
    private final CertificateConverter certificateConverter;
    private final CertificateAssembler certificateAssembler;

    @Autowired
    public CertificateController(GiftCertificateService certificateService, CertificateAssembler certificateAssembler,CertificateConverter certificateConverter) {
        this.certificateService = certificateService;
        this.certificateConverter = certificateConverter;
        this.certificateAssembler = certificateAssembler;
    }

    @GetMapping
    public CollectionModel<CertificateModel> getAllByRequestParams(@RequestParam MultiValueMap<String,String> params,
                                                                   @RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                                                   @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit) {
        List<GiftCertificate> certs = certificateService.handleParametrizedGetRequest(params,page,limit);
        return certificateAssembler.toCollectionModel(certs);
    }

    @GetMapping(value = "/{id:\\d+}")
    public CertificateModel getByID(@PathVariable long id) {//
        return certificateAssembler.toModel(certificateService.getByID(id));
    }

    @DeleteMapping(value = "/{id:\\d+}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable long id){ //
        certificateService.deleteByID(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = "/{id:\\d+}")
    public ResponseEntity<CertificateModel> updateCertificate(@RequestBody @Validated(PatchDTO.class) GiftCertificateDto certificateDtoPatch,
                                                              @PathVariable long id){//
        GiftCertificate certificate = certificateService.update(
        certificateConverter.convertFromRequestDto(certificateDtoPatch),id);
        return new ResponseEntity<>(certificateAssembler.toModel(certificate),HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> createCertificate(@RequestBody @Validated(CreateDTO.class) GiftCertificateDto certificateDto) {//
        GiftCertificate updatedEntity = certificateService.addEntity(certificateConverter.convertFromRequestDto(certificateDto));
        return new ResponseEntity<>(certificateAssembler.toModel(updatedEntity),HttpStatus.CREATED);
    }

}

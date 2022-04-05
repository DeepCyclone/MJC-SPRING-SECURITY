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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary =  "Take all available certificates by pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Print all existing certificates with params",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = CertificateModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid pagination params.Duplicating fields:parts of tag name or certificate description,sorting orders.Illegal values for sorting orders",
            content = @Content)    
    })
    @GetMapping
    public CollectionModel<CertificateModel> getAllByRequestParams(@Parameter(description = "params to concretize searching:tag names,tag name part and it's sorting order,cert description part,cert creation date sort order") @RequestParam MultiValueMap<String,String> params,
                                                                   @Parameter(description = "page of result") @RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                                                   @Parameter(description = "records per page") @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit) {
        List<GiftCertificate> certs = certificateService.handleParametrizedGetRequest(params,page,limit);
        return certificateAssembler.toCollectionModel(certs);
    }

    @Operation(summary =  "Get certificate by ID with links to associated tags if there are present")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Certificate found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = CertificateModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Certificate not found",
            content =  @Content),
    })
    @GetMapping(value = "/{id:\\d+}")
    public CertificateModel getByID(@Parameter(description = "id of certificate to be searched") @PathVariable long id) {
        return certificateAssembler.toModel(certificateService.getByID(id));
    }

    
    @Operation(summary =  "delete certificate by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" , description = "Certificate deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Certificate not found",
            content =  @Content),
        @ApiResponse(responseCode = "409" , description = "Unable to delete certificare due to connections to order(s)",
            content =  @Content)
    })
    @DeleteMapping(value = "/{id:\\d+}")
    public ResponseEntity<Void> deleteCertificate(@Parameter(description = "id of certificate to be deleted") @PathVariable long id){
        certificateService.deleteByID(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary =  "update certificate's data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201" , description = "Certificate update successfully",
            content = @Content),
        @ApiResponse(responseCode = "409" , description = "Conflict when updating.Check params",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Certificate not found",
            content =  @Content),
        @ApiResponse(responseCode = "400" , description = "Bad patch params. Check schema of GiftCertificateDto",
            content =  @Content)
            
    })
    @PatchMapping(value = "/{id:\\d+}")
    public ResponseEntity<CertificateModel> updateCertificate(@RequestBody @Validated(PatchDTO.class) GiftCertificateDto certificateDtoPatch,
                                                              @PathVariable long id){
        GiftCertificate certificate = certificateService.update(
        certificateConverter.convertFromRequestDto(certificateDtoPatch),id);
        return new ResponseEntity<>(certificateAssembler.toModel(certificate),HttpStatus.CREATED);
    }

    @Operation(summary =  "create certificate")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201" , description = "Certificate created successfully",
            content = @Content),
        @ApiResponse(responseCode = "409" , description = "Conflict when creating.Check params",
            content =  @Content),
        @ApiResponse(responseCode = "400" , description = "Bad request body params. Check schema of GiftCertificateDto",
            content =  @Content)
            
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateModel> createCertificate(@RequestBody @Validated(CreateDTO.class) GiftCertificateDto certificateDto) {//
        GiftCertificate updatedEntity = certificateService.addEntity(certificateConverter.convertFromRequestDto(certificateDto));
        return new ResponseEntity<>(certificateAssembler.toModel(updatedEntity),HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/cache")
    public ResponseEntity<Void> clearCache(){
        certificateService.clearCache();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}

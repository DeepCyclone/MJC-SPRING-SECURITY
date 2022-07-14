package com.epam.esm.controller;

import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.PatchDTO;
import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.hateoas.assembler.CertificateAssembler;
import com.epam.esm.hateoas.assembler.OrderAssembler;
import com.epam.esm.hateoas.assembler.TagAssembler;
import com.epam.esm.hateoas.model.CertificateModel;
import com.epam.esm.hateoas.model.OrderModel;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.hateoas.processor.CertificateProcessor;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.service.GiftCertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/api/v1/certificates",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CertificateController {

    private final GiftCertificateService certificateService;
    private final CertificateConverter certificateConverter;
    private final CertificateAssembler certificateAssembler;
    private final CertificateProcessor certificateProcessor;
    private final TagAssembler tagAssembler;
    private final OrderAssembler orderAssembler;

    @Autowired
    public CertificateController(GiftCertificateService certificateService, CertificateConverter certificateConverter, CertificateAssembler certificateAssembler, CertificateProcessor certificateProcessor, TagAssembler tagAssembler, OrderAssembler orderAssembler) {
        this.certificateService = certificateService;
        this.certificateConverter = certificateConverter;
        this.certificateAssembler = certificateAssembler;
        this.certificateProcessor = certificateProcessor;
        this.tagAssembler = tagAssembler;
        this.orderAssembler = orderAssembler;
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
    public CollectionModel<CertificateModel> getAllByRequestParams(@Parameter(description = "part of certificate name") @RequestParam(name = "namePart",defaultValue = "",required = false) String certificateNamePart,
                                                                   @Parameter(description = "part of certificate description") @RequestParam(name = "descriptionPart",defaultValue = "",required = false) String descriptionPart,
                                                                   @Parameter(description = "names of tags associated with certificates") @RequestParam(name = "tagName",required = false) Set<String> tagsNames,
                                                                   @Parameter(description = "sort order by certificate name") @RequestParam(required = false,name = "nameSortOrder",defaultValue = "") String certificateNameSortOrder,
                                                                   @Parameter(description = "sort order by certificate creation date") @RequestParam(required = false,name = "dateSortOrder",defaultValue = "") String certificateCreationDateSortOrder,
                                                                   @Parameter(description = "page of result") @RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                                                   @Parameter(description = "records per page") @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit) {
        Page<GiftCertificate> certs = certificateService.handleParametrizedGetRequest(certificateNamePart,
                                                                                      descriptionPart,
                                                                                      tagsNames,
                                                                                      certificateNameSortOrder,
                                                                                      certificateCreationDateSortOrder,
                                                                                      page,
                                                                                      limit);
        CollectionModel<CertificateModel> certificateModels = certificateAssembler.toCollectionModel(certs);
        certificateProcessor.process(certificateModels,certs,page,limit,certificateNamePart,descriptionPart,tagsNames,certificateNameSortOrder,certificateCreationDateSortOrder);
        return certificateModels;
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

//    @GetMapping(value = "/{id:\\d+}")
//    public String getByID(@Parameter(description = "id of certificate to be searched") @PathVariable long id) {
//        return JWTCodec.createJWT(Long.toString(id),"cert","get by id",1000000);
//    }

    @GetMapping(value = "/{id:\\d+}/tags")
    public CollectionModel<TagModel> getAssociatedTags(@PathVariable long id){
        return tagAssembler.toCollectionModel(certificateService.getByID(id).getAssociatedTags());
    }

    @GetMapping(value = "/{id:\\d+}/orders")
    public CollectionModel<OrderModel> getAssociatedOrders(@PathVariable long id){
        return orderAssembler.toCollectionModel(certificateService.getByID(id).getAssociatedOrders());
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
    public ResponseEntity<CertificateModel> createCertificate(@RequestBody @Validated(CreateDTO.class) GiftCertificateDto certificateDto) {
        GiftCertificate updatedEntity = certificateService.addEntity(certificateConverter.convertFromRequestDto(certificateDto));
        return new ResponseEntity<>(certificateAssembler.toModel(updatedEntity),HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/cache")
    public ResponseEntity<Void> clearCache(){
        certificateService.clearCache();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    
}

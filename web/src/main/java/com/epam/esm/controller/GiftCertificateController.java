package com.epam.esm.controller;

import com.epam.esm.dto.request.GiftCertificateDto;
import com.epam.esm.dto.request.TagDto;
import com.epam.esm.exception.ObjectNotFoundException;
import com.epam.esm.repository.model.GiftCertificate;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.converter.CertificateDtoConverter;
import com.epam.esm.service.impl.GiftCertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/certificates",produces = {MediaType.APPLICATION_JSON_VALUE})
public class GiftCertificateController {

    private final GiftCertificateService service;
    private final CertificateDtoConverter converter;

    @Autowired
    public GiftCertificateController(GiftCertificateService service, CertificateDtoConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping
    public List<GiftCertificate> getAll(){
        return service.getAll();
    }

    @GetMapping(value = "/{id:\\d+}")
    public GiftCertificate getByID(@PathVariable long id,
                                   @RequestParam(required = false) String tagName,
                                   @RequestParam(required = false) String namePart,
                                   @RequestParam(required = false) String descriptionPart,
                                   @RequestParam(required = false,defaultValue = "ASC") String dateSortOrder,
                                   @RequestParam(required = false,defaultValue = "ASC") String nameSortOrder){
        GiftCertificate giftCertificate = service.getByID(id);
        if(giftCertificate == null){throw new ObjectNotFoundException("Object with ID:"+id+" wasn't found",id);}
        return giftCertificate;
    }


    @DeleteMapping(value = "/{id:\\d+}")
    public void deleteCertificate(@PathVariable long id){
        service.deleteByID(id);
    }

    @PatchMapping(value = "/{id:\\d+}")
    public void updateCertificate(@PathVariable long id,@RequestBody GiftCertificateDto certificateDtoPatch){
        service.update(certificateDtoPatch,id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public GiftCertificate createCertificate(@RequestBody GiftCertificateDto certificateDto){
        List<Tag> tags = new ArrayList<>();
        for(TagDto dto:certificateDtoPatch.getAssociatedTags()) {
            tags.add(tagRepository.create(converter.convertFromDto(dto)));
        }
        linkAssociatedTags(certificateID,tags);
        GiftCertificate certificate = converter.convertFromDto(certificateDto);
        return service.addEntity(certificate);//responseDtoMapping
    }

}

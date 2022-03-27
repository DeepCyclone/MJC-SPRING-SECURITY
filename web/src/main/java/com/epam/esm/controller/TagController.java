package com.epam.esm.controller;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.request.TagDto;
import com.epam.esm.dto.response.TagResponseDto;
import com.epam.esm.hateoas.assembler.TagAssembler;
import com.epam.esm.hateoas.model.TagModel;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.service.template.TagService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.constraints.Min;

@RestController
@RequestMapping(value = "/api/v1/tags",produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class TagController {


    private final TagService tagService;
    private final TagConverter tagConverter;
    private final TagAssembler tagAssembler;

    @Autowired
    public TagController(TagService tagService,TagConverter tagConverter,TagAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagConverter = tagConverter;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping(value = "/{id:\\d+}")
    public TagModel getByID(@PathVariable("id") Long id){
        return tagAssembler.toModel(tagService.getByID(id));
    }

    @GetMapping
    public CollectionModel<TagModel> getTags(@RequestParam(defaultValue = "1",name = "page") @Min(value = 1,message = "page >=1 ") Integer page,
                                        @RequestParam(defaultValue = "10" ,name = "limit") @Min(value = 1,message = "limit >=1 ") Integer limit){//
        return tagAssembler.toCollectionModel(tagService.getAll(page,limit));
    }

    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable("id") Long id){//
        tagService.deleteByID(id);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagModel createTag(@RequestBody @Validated(CreateDTO.class) TagDto tagDto){//
        Tag tag = tagConverter.convertFromRequestDto(tagDto);
        return tagAssembler.toModel(tagService.addEntity(tag));
    }

}

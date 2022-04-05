package com.epam.esm.controller;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.request.TagDto;
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

import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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

    @Operation(summary =  "Get certificate by ID with links to associated tags if there are present")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Certificate found",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = TagModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Certificate not found",
            content =  @Content),
    })
    @GetMapping(value = "/{id:\\d+}")
    public TagModel getByID(@Parameter(description = "id of tag to be searched") @PathVariable("id") Long id){
        return tagAssembler.toModel(tagService.getByID(id));
    }

    @Operation(summary =  "Take all available tags by pages")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200" , description = "Print all existing tags with params",
            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema =  @Schema(implementation = TagModel.class))}),
        @ApiResponse(responseCode = "400" , description = "Invalid pagination params",
            content = @Content) 
    })
    @GetMapping
    public CollectionModel<TagModel> getTags(@Parameter(description = "page of result") @RequestParam(defaultValue = "1") @Min(value = 1,message = "page >=1 ") Integer page,
                                             @Parameter(description = "records per page") @RequestParam(defaultValue = "10") @Min(value = 1,message = "limit >=1 ") Integer limit){
        return tagAssembler.toCollectionModel(tagService.getAll(page,limit));
    }

    @Operation(summary =  "delete tag by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204" , description = "Tag deleted successfully",
            content = @Content),
        @ApiResponse(responseCode = "400" , description = "Invalid ID. Provide positive ID to link",
            content =  @Content),
        @ApiResponse(responseCode = "404" , description = "Tag not found",
            content =  @Content),
        @ApiResponse(responseCode = "409" , description = "Unable to delete tag due to connections to certificates",
            content =  @Content)
    })
    @DeleteMapping(value = "/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@Parameter(description = "id of tag to be deleted") @PathVariable("id") Long id){
        tagService.deleteByID(id);
    }


    @Operation(summary =  "create Tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201" , description = "Tag created successfully",
            content = @Content),
        @ApiResponse(responseCode = "409" , description = "Conflict when creating.Check params",
            content =  @Content),
        @ApiResponse(responseCode = "400" , description = "Bad request body params. Check schema of TagDto",
            content =  @Content)
            
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TagModel createTag(@RequestBody @Validated(CreateDTO.class) TagDto tagDto){
        Tag tag = tagConverter.convertFromRequestDto(tagDto);
        return tagAssembler.toModel(tagService.addEntity(tag));
    }

}

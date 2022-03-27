package com.epam.esm.hateoas.model;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagModel extends RepresentationModel<TagModel> implements Serializable {
    private long id;
    private String name;
}

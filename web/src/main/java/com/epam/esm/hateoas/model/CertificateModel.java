package com.epam.esm.hateoas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Gift certificate response dto",description = "Container of gift certificate properties with links to associated tags")
public class CertificateModel extends RepresentationModel<CertificateModel> implements Serializable{
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private int duration;
    @Schema(name = "Creation date",description = "certificate creation date in ISO 8601 format")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date createDate;
    @Schema(name = "Last modification date",description = "certificate data last modifictaion date in ISO 8601 format")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date lastUpdateDate;
}

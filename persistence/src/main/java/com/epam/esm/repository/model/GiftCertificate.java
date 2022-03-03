package com.epam.esm.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificate implements Serializable {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Timestamp createDate;
    private Timestamp lastUpdateDate;
    List<Tag> associatedTags;
}

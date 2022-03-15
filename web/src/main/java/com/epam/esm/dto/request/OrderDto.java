package com.epam.esm.dto.request;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private long id;
    private BigDecimal price;
    private Timestamp purchaseDate;
    private List<GiftCertificateDto> certificates;
}

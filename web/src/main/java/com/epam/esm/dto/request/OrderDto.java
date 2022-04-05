package com.epam.esm.dto.request;

import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.PatchDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
@Schema(name = "Order request DTO")
public class OrderDto implements Serializable{
    @Null(groups = {CreateDTO.class},message = "ID will be created automatically.Remove it")
    @Null(message = "Please specify id of object to be patched in URL",groups = {PatchDTO.class})
    private Long id;
    @Null(groups = {CreateDTO.class},message = "Price will be counted as sum of all orders prices")
    @PositiveOrZero(message = "Price values must be in [0;+inf)",groups = {PatchDTO.class})
    private BigDecimal price;
    private List<GiftCertificateDto> certificates;
}

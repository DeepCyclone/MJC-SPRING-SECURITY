package com.epam.esm.dto.response;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto extends RepresentationModel<UserResponseDto> implements Serializable {
    private long id;
    private String name;
    private List<OrderResponseDto> orders;
}

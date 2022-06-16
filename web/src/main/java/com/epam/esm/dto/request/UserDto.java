package com.epam.esm.dto.request;

import com.epam.esm.dto.CreateDTO;
import com.epam.esm.dto.PatchDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "User request DTO")
public class UserDto implements Serializable {
    @PositiveOrZero(message = "ID should be positive number",groups = {CreateDTO.class, PatchDTO.class})
    private long id;
    @NotBlank(message = "username not blank constraints",groups = {CreateDTO.class, PatchDTO.class})
    @Size(min = 2,max = 15,message = "username length constraints = [2,15]",groups = {CreateDTO.class,PatchDTO.class})
    private String name;
    @NotBlank(message = "user's password not blank constraints",groups = {CreateDTO.class, PatchDTO.class})
    @Size(min = 2,max = 15,message = "user's password length constraints = [2,15]",groups = {CreateDTO.class,PatchDTO.class})
    private String password;
}

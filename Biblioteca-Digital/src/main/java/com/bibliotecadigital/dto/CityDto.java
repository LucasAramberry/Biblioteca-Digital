package com.bibliotecadigital.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

    private Long id;

    @NotBlank(message = "El nombre de la ciudad no puede ser nulo.")
    private String name;
}

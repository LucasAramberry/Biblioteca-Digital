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
public class PublisherDto {

    private String id;

    @NotBlank(message = "El nombre de la editorial no puede ser nulo.")
    private String name;

    private PhotoDto photoDto;
}

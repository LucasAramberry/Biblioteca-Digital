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
public class AuthorDto {

    private String id;

    @NotBlank(message = "El nombre del autor no puede ser nulo.")
    private String name;

    private PhotoDto photoDto;
}

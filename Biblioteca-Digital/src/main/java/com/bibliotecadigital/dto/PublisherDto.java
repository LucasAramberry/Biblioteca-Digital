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

    @NotBlank(message = "Name of publisher cannot be null.")
    private String name;

    private PhotoDto photoDto;
}

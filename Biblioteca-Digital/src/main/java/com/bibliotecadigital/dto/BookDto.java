package com.bibliotecadigital.dto;

import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Photo;
import com.bibliotecadigital.entity.Publisher;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {

    private String id;

    @NotBlank(message = "El isbn no puede ser nulo y debe contener 13 caracteres")
    @Size(min = 13, max = 13, message = "El isbn no puede ser nulo y debe contener 13 caracteres")
    private String isbn;

    @NotBlank(message = "El titulo no puede ser nulo")
    private String title;

    @NotBlank(message = "La descripcion no puede ser nula")
    @Size(min = 50, max = 255, message = "La descripcion no puede ser nula")
    private String description;

    @Future(message = "Fecha de publicacion invalida.")
    private LocalDate datePublisher;

    @NotNull(message = "Cantidad de paginas invalida.")
    @Min(value = 5, message = "Cantidad de paginas invalida.")
    private Integer amountPages;

    @NotNull(message = "Cantidad de ejemplares invalida.")
    @Min(value = 1, message = "Cantidad de ejemplares invalida.")
    private Integer amountCopies;

    @NotNull(message = "Cantidad de ejemplares prestados invalida.")
    @Min(value = 0, message = "Cantidad de ejemplares prestados invalida.")
    private Integer amountCopiesBorrowed;

    @NotNull()
    private Integer amountCopiesRemaining;

    private PhotoDto photoDto;

    private Author author;

    private Publisher publisher;
}

package com.bibliotecadigital.dto;

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

    @NotBlank(message = "Isbn cannot be null")
    @Size(min = 13, max = 13, message = "Isbn must contains 13 digits")
    private String isbn;

    @NotBlank(message = "Titulo cannot be null")
    private String title;

    @NotBlank(message = "Description cannot be null")
    @Size(min = 25, max = 255, message = "Description contains min 25 characters")
    private String description;

    @NotNull(message = "Date of publisher cannot be null")
    @PastOrPresent(message = "Date of publisher is invalid.")
    private LocalDate datePublisher;

    @NotNull(message = "Amount of pages is invalid.")
    @Min(value = 5, message = "Amount of pages cannot be < 5.")
    private Integer amountPages;

    @NotNull(message = "Amount of copies invalid.")
    @Min(value = 1, message = "Amount of copies invalid.")
    private Integer amountCopies;

    @NotNull(message = "Amount of copies borrowed invalid.")
    @Min(value = 0, message = "Amount of copies borrowed invalid.")
    private Integer amountCopiesBorrowed;

    private Integer amountCopiesRemaining;

    private PhotoDto photoDto;

    @NotBlank(message = "Author cannot be null.")
    private String idAuthor;

    @NotBlank(message = "Publisher cannot be null.")
    private String idPublisher;
}

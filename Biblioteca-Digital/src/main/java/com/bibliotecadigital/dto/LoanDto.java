package com.bibliotecadigital.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {

    private Long id;

    private LocalDate dateLoan;

    @NotNull(message = "Date devolution cannot be null.")
    @Future(message = "The return date cannot be present or past.")
    private LocalDate dateReturn;

    @NotBlank(message = "The book cannot be null.")
    private String idBook;

//    @NotBlank(message = "The user cannot be null.")
    private String idUser;
}

package com.bibliotecadigital.dto;

import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
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

    @Future(message = "The return date cannot be present or past.")
    private LocalDate dateReturn;

    @NotBlank(message = "The book cannot be null.")
    private String idBook;

    private User user;
}

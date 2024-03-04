package com.bibliotecadigital.dto;

import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.User;
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

    private LocalDate dateReturn;

    private Book book;

    private User user;
}

package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.error.ErrorException;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ILoanService {

    void register(LoanDto loanDto) throws ErrorException;

    void update(LoanDto loanDto) throws ErrorException;

    void delete(Long id) throws ErrorException;

    void high(Long id) throws ErrorException;

    void low(Long id) throws ErrorException;

    void devolution(Long id) throws ErrorException;

    List<Loan> findByUser(@Param("id") String id);

    List<Loan> findByActive();

    List<Loan> findByInactive();

    List<Loan> findAll();

    Loan findById(Long id) throws ErrorException;

    void save(Loan loan);

    void deleteById(Long id);
}

package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.error.ErrorException;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILoanService {

    void register(LoanDto loanDto) throws ErrorException;

    void update(Long id, LoanDto loanDto) throws ErrorException;

    void delete(Long id);

    void high(Long id) throws ErrorException;

    void low(Long id);

    void devolution(Long id);

    public List<Loan> findByUser(@Param("id") String id);

    public List<Loan> findByActive();

    public List<Loan> findByInactive();

    List<Loan> findAll();

    Optional<Loan> findById(Long id);

    void save(Loan loan);

    void deleteById(Long id);
}

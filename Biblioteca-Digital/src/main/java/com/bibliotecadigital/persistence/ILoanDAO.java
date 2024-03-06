package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entities.Loan;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILoanDAO {

    List<Loan> findByUser(@Param("id") String id);

    List<Loan> findByActive();

    List<Loan> findByInactive();

    List<Loan> findAll();

    Optional<Loan> findById(Long id);

    void save(Loan loan);

    void delete(Loan loan);

    void deleteById(Long id);
}

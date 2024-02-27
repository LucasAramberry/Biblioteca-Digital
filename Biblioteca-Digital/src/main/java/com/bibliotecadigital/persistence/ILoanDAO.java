package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entity.City;
import com.bibliotecadigital.entity.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ILoanDAO {

    public List<Loan> findByUser(@Param("id") String id);

    public List<Loan> findByActive();

    public List<Loan> findByInactive();

    List<Loan> findAll();

    Optional<Loan> findById(Long id);

    void save(Loan loan);

    void delete(Loan loan);

    void deleteById(Long id);
}

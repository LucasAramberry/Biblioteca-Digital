package com.bibliotecadigital.repository;

import com.bibliotecadigital.entity.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {

    // Devuelve los prestamos de un usuario
    @Query("SELECT p FROM Loan p WHERE p.user.id = :id")
    public List<Loan> findByUser(@Param("id") String id);

    // Devuelve una Lista con prestamos activos.
    @Query("SELECT p FROM Loan p WHERE p.unsubscribe IS null")
    public List<Loan> findByActive();

    // Devuelve una Lista con prestamos dados de baja.
    @Query("SELECT p FROM Loan p WHERE p.unsubscribe IS NOT null")
    public List<Loan> findByInactive();
}

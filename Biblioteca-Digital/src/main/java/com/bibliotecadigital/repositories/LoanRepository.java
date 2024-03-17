package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.Loan;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long> {

    // Return list loans by user
    @Query("SELECT p FROM Loan p WHERE p.user.id = :id")
    List<Loan> findByUser(@Param("id") String id);

    // Return list loans active.
    @Query("SELECT p FROM Loan p WHERE p.unsubscribe IS null")
    List<Loan> findByActive();

    // Return list loans inactive.
    @Query("SELECT p FROM Loan p WHERE p.unsubscribe IS NOT null")
    List<Loan> findByInactive();
}

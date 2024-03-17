package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {

    // Return list with authors active.
    @Query("SELECT a FROM Author a WHERE a.unsubscribe IS null")
    List<Author> findByActive();

    // Return list with authors inactive.
    @Query("SELECT a FROM Author a WHERE a.unsubscribe IS NOT null")
    List<Author> findByInactive();

    // Return author for name
    Optional<Author> findByName(String name);
}

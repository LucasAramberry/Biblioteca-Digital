package com.bibliotecadigital.repository;

import com.bibliotecadigital.entity.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {

    // Devuelve una Lista con autores activos.
    @Query("SELECT a FROM Author a WHERE a.unsubscribe IS null")
    public List<Author> findByActive();

    // Devuelve una Lista con autores dados de baja.
    @Query("SELECT a FROM Author a WHERE a.unsubscribe IS NOT null")
    public List<Author> findByInactive();

    // Devuelve autor por nombre
    @Query("SELECT a FROM Author a WHERE a.name = :name")
    public Author findByName(@Param("name") String name);
}

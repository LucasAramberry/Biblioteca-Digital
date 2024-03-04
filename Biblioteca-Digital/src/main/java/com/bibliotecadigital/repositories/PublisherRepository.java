package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, String> {

    // Devuelve una Lista con editoriales activos.
    @Query("SELECT e FROM Publisher e WHERE e.unsubscribe IS null")
    public List<Publisher> findByActive();

    // Devuelve una Lista con editoriales dados de baja.
    @Query("SELECT e FROM Publisher e WHERE e.unsubscribe IS NOT null")
    public List<Publisher> findByInactive();

    // Devuelve editorial por nombre
    @Query("SELECT e FROM Publisher e WHERE e.name = :name")
    public Publisher findByName(@Param("name") String name);
}

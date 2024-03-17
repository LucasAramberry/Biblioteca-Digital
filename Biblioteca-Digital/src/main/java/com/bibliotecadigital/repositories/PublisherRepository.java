package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PublisherRepository extends CrudRepository<Publisher, String> {

    // Return list publishers active.
    @Query("SELECT e FROM Publisher e WHERE e.unsubscribe IS null")
    List<Publisher> findByActive();

    // Return list publishers inactive.
    @Query("SELECT e FROM Publisher e WHERE e.unsubscribe IS NOT null")
    List<Publisher> findByInactive();

    // Return publisher by name
    Optional<Publisher> findByName(String name);
}

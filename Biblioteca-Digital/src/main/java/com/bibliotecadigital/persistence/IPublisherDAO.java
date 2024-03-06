package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entities.Publisher;

import java.util.List;
import java.util.Optional;

public interface IPublisherDAO {

    List<Publisher> findByActive();

    List<Publisher> findByInactive();

    Publisher findByName(String name);

    List<Publisher> findAll();

    Optional<Publisher> findById(String id);

    void save(Publisher publisher);

    void delete(Publisher publisher);

    void deleteById(String id);
}

package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entities.Author;

import java.util.List;
import java.util.Optional;

public interface IAuthorDAO {

    List<Author> findByActive();

    List<Author> findByInactive();

    Optional<Author> findByName(String name);

    List<Author> findAll();

    Optional<Author> findById(String id);

    void save(Author author);

    void delete(Author author);

    void deleteById(String id);

}

package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.entities.Author;

import java.util.List;

public interface IAuthorService {

    void register(AuthorDto authorDto);

    void update(String id, AuthorDto authorDto);

    void delete(String id);

    void high(String id);

    void low(String id);

    List<Author> findByActive();

    List<Author> findByInactive();

    Author findByName(String name);

    List<Author> findAll();

    Author findById(String id);

    void save(Author author);


    void deleteById(String id);

}

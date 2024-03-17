package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;

public interface IAuthorService {

    void register(AuthorDto authorDto);

    void update(AuthorDto authorDto) throws ErrorException;

    void delete(String id) throws ErrorException;

    void high(String id) throws ErrorException;

    void low(String id) throws ErrorException;

    List<Author> findByActive();

    List<Author> findByInactive();

    Author findByName(String name);

    List<Author> findAll();

    Author findById(String id) throws ErrorException;

    void save(Author author);


    void deleteById(String id);

}

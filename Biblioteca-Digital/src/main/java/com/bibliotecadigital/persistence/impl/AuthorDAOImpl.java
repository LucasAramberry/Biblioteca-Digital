package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.persistence.IAuthorDAO;
import com.bibliotecadigital.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AuthorDAOImpl implements IAuthorDAO {

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<Author> findByActive() {
        return authorRepository.findByActive();
    }

    @Override
    public List<Author> findByInactive() {
        return authorRepository.findByInactive();
    }

    @Override
    public Optional<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public List<Author> findAll() {
        return (List<Author>) authorRepository.findAll();
    }

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public void save(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

    @Override
    public void deleteById(String id) {

    }
}

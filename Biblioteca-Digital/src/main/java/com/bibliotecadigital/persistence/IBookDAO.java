package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IBookDAO {

    List<Book> findByAuthor(String id);

    List<Book> findByPublisher(String id);

    List<Book> listBookActive();

    List<Book> listBookInactive();

    List<Book> findAll();

    Optional<Book> findById(String id);

    void save(Book book);

    void delete(Book book);

    void deleteById(String id);
}

package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;
import java.util.Optional;

public interface IBookService {

    void register(BookDto bookDto);

    void update(String id, BookDto bookDto);

    void delete(String id);

    void high(String id);

    void low(String id);

    void lendBook(Book book) throws ErrorException;

    void devolutionBook(Book book);

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

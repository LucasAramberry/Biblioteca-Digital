package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;

public interface IBookService {

    void register(BookDto bookDto) throws ErrorException;

    void update(BookDto bookDto) throws ErrorException;

    void delete(String id) throws ErrorException;

    void high(String id) throws ErrorException;

    void low(String id) throws ErrorException;

    void lendBook(Book book) throws ErrorException;

    void devolutionBook(Book book);

    List<Book> findByAuthor(String id);

    List<Book> findByPublisher(String id);

    List<Book> listBookActive();

    List<Book> listBookInactive();

    List<Book> findAll();

    Book findById(String id) throws ErrorException;

    void save(Book book);

    void delete(Book book);

}

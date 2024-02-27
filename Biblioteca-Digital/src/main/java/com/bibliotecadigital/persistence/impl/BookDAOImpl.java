package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entity.Book;
import com.bibliotecadigital.persistence.IBookDAO;
import com.bibliotecadigital.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAOImpl implements IBookDAO {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<Book> findByAuthor(String id) {
        return bookRepository.findByAuthor(id);
    }

    @Override
    public List<Book> findByPublisher(String id) {
        return bookRepository.findByPublisher(id);
    }

    @Override
    public List<Book> listBookActive() {
        return bookRepository.listBookActive();
    }

    @Override
    public List<Book> listBookInactive() {
        return bookRepository.listBookInactive();
    }

    @Override
    public List<Book> findAll() {
        return (List<Book>) bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookRepository.findById(id);
    }

    @Override
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }
}

package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.entities.*;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IBookDAO;
import com.bibliotecadigital.services.IAuthorService;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.IPhotoService;
import com.bibliotecadigital.services.IPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Service
@Slf4j
public class BookServiceImpl implements IBookService {

    @Autowired
    private IBookDAO bookDAO;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublisherService publisherService;
    @Autowired
    private IPhotoService photoService;

    /**
     * Method for create book
     *
     * @param bookDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void register(BookDto bookDto) throws ErrorException {

        Author author = authorService.findById(bookDto.getIdAuthor());
        Publisher publisher = publisherService.findById(bookDto.getIdPublisher());

        save(Book
                .builder()
                .isbn(bookDto.getIsbn())
                .title(bookDto.getTitle())
                .description(bookDto.getDescription())
                .datePublisher(bookDto.getDatePublisher())
                .amountPages(bookDto.getAmountPages())
                .amountCopies(bookDto.getAmountCopies())
                .amountCopiesBorrowed(bookDto.getAmountCopiesBorrowed())
                .amountCopiesRemaining((bookDto.getAmountCopies() - bookDto.getAmountCopiesBorrowed()))
                .register(LocalDateTime.now())
                .author(author)
                .publisher(publisher)
                .photo(photoService.register(bookDto.getPhotoDto()))
                .build()
        );

        log.info("Create Book");
    }

    /**
     * Method for update book
     *
     * @param bookDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void update(BookDto bookDto) throws ErrorException {

        Book book = findById(bookDto.getId());

        book.setIsbn(bookDto.getIsbn());
        book.setTitle(bookDto.getTitle());
        book.setDescription(bookDto.getDescription());
        book.setDatePublisher(bookDto.getDatePublisher());
        book.setAmountPages(bookDto.getAmountPages());
        book.setAmountCopies(bookDto.getAmountCopies());
        book.setAmountCopiesBorrowed(bookDto.getAmountCopiesBorrowed());
        book.setAmountCopiesRemaining(bookDto.getAmountCopies() - bookDto.getAmountCopiesBorrowed());
        book.setAuthor(((book.getAuthor().getId() != bookDto.getIdAuthor()) ? authorService.findById(bookDto.getIdAuthor()) : book.getAuthor()));
        book.setPublisher(((book.getPublisher().getId() != bookDto.getIdPublisher()) ? publisherService.findById(bookDto.getIdPublisher()) : book.getPublisher()));

        Photo photo = null;
        if (book.getPhoto() != null && !bookDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.update(book.getPhoto().getId(), bookDto.getPhotoDto());
        } else if (book.getPhoto() == null && !bookDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.register(bookDto.getPhotoDto());
        }
        book.setPhoto(photo);

        save(book);

        log.info("Update Book");
    }

    /**
     * Method for delete book
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {

        Book book = findById(id);
        delete(book);

        log.info("Delete Book with id " + id);
    }

    /**
     * Method for activate book
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {

        Book book = findById(id);
        if (book.getUnsubscribe() != null) {
            book.setUnsubscribe(null);
            save(book);
        }
        log.info("High Author");
    }

    /**
     * Method for unsubscribe book
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {

        Book book = findById(id);
        if (book.getUnsubscribe() == null) {
            book.setUnsubscribe(LocalDateTime.now());
            save(book);
        }

        log.info("Low Book with id " + id);
    }

    /**
     * Method for loan a book. Subtraction the copies remaining and add the copies borrowed
     *
     * @param book
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void lendBook(Book book) throws ErrorException {
        book.setAmountCopiesBorrowed(book.getAmountCopiesBorrowed() + 1);
        book.setAmountCopiesRemaining(book.getAmountCopiesRemaining() - 1);
        save(book);
        log.info("Book lend");
    }

    /**
     * Method for restore loan of book
     *
     * @param book
     */
    @Transactional
    @Override
    public void devolutionBook(Book book) {
        book.setAmountCopiesBorrowed(book.getAmountCopiesBorrowed() - 1);
        book.setAmountCopiesRemaining(book.getAmountCopiesRemaining() + 1);
        save(book);
        log.info("Book devolution");
    }

    /**
     * Method find books by author
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findByAuthor(String id) {
        return bookDAO.findByAuthor(id);
    }

    /**
     * Method find books by publisher
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findByPublisher(String id) {
        return bookDAO.findByPublisher(id);
    }

    /**
     * Method find all books active
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> listBookActive() {
        return bookDAO.listBookActive();
    }

    /**
     * Method find all books inactive
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> listBookInactive() {
        return bookDAO.listBookInactive();
    }

    /**
     * Method find all books
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Book> findAll() {
        return bookDAO.findAll();
    }

    /**
     * Method find book by id
     *
     * @param id
     * @return
     * @throws ErrorException
     */
    @Transactional(readOnly = true)
    @Override
    public Book findById(String id) throws ErrorException {
        return bookDAO.findById(id).orElseThrow(() -> new ErrorException("The book with id " + id + " was not found."));
    }

    /**
     * Method save book
     *
     * @param book
     */
    @Transactional
    @Override
    public void save(Book book) {
        bookDAO.save(book);
    }

    /**
     * Method for delete book
     *
     * @param book
     */
    @Transactional
    @Override
    public void delete(Book book) {
        bookDAO.delete(book);
    }

}

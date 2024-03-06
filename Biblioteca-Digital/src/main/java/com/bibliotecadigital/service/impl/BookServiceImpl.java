package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.entities.*;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IBookDAO;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.IPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private PhotoServiceImpl photoService;

    /**
     * Metodo para registrar book
     *
     * @param bookDto
     */
    @Transactional
    @Override
    public void register(BookDto bookDto) {

        Author author = authorService.findById(bookDto.getIdAuthor());

        Publisher publisher = publisherService.findById(bookDto.getIdPublisher());

        Photo photo = photoService.register(bookDto.getPhotoDto());

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
                .author(((author != null) ? author : null))
                .publisher(((publisher != null) ? publisher : null))
                .photo(photo)
                .build()
        );
    }

    /**
     * metodo para modificar book
     *
     * @param id
     * @param bookDto
     */
    @Transactional
    @Override
    public void update(String id, BookDto bookDto) {

        Optional<Book> response = bookDAO.findById(id);

        if (response.isPresent()) {

            Book book = response.get();

            Author author = authorService.findById(bookDto.getIdAuthor());

            Publisher publisher = publisherService.findById(bookDto.getIdPublisher());

            book.setIsbn(bookDto.getIsbn());
            book.setTitle(bookDto.getTitle());
            book.setDescription(bookDto.getDescription());
            book.setDatePublisher(bookDto.getDatePublisher());
            book.setAmountPages(bookDto.getAmountPages());
            book.setAmountCopies(bookDto.getAmountCopies());
            book.setAmountCopiesBorrowed(bookDto.getAmountCopiesBorrowed());
            //validar que no sean menos los totales q los prestados
            book.setAmountCopiesRemaining(bookDto.getAmountCopies() - bookDto.getAmountCopiesBorrowed());
            book.setAuthor(((author != null) ? author : null));
            book.setPublisher(((publisher != null) ? publisher : null));

            Long idPhoto = null;

            if (book.getPhoto() != null) {
                idPhoto = book.getPhoto().getId();
            }

            Photo photo = photoService.update(idPhoto, bookDto.getPhotoDto());

            book.setPhoto(photo);

            save(book);

        } else {
            log.error("No se encontro el libro solicitado para modificar.");
        }
    }

    /**
     * metodo para eliminar book
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) {

        Optional<Book> response = findById(id);

        if (response.isPresent()) {
            Book book = response.get();
            bookDAO.delete(book);

        } else {
            log.error("No se encontro el libro solicitado para eliminar.");
        }
    }

    /**
     * Metodo para habilitar book
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(String id) {
        Optional<Book> response = bookDAO.findById(id);

        if (response.isPresent()) {
            Book book = response.get();
            book.setUnsubscribe(null);

            bookDAO.save(book);
        } else {
            log.error("Error al habilitar el libro solicitado.");
        }
    }

    /**
     * Metodo para deshabilitar book
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(String id) {
        Optional<Book> response = bookDAO.findById(id);

        if (response.isPresent()) {
            Book book = response.get();
            book.setUnsubscribe(LocalDateTime.now());

            bookDAO.save(book);
        } else {
            log.error("Error al deshabilitar el libro solicitado.");
        }
    }

    /**
     * Metodo prestamo book. Cuando prestamos restamos los restantes y sumamos a prestados
     *
     * @param book
     */
    @Transactional
    @Override
    public void lendBook(Book book) throws ErrorException {
        if (book != null) {
            if (book.getAmountCopiesBorrowed() >= 1) {
                book.setAmountCopiesBorrowed(book.getAmountCopiesBorrowed() + 1);
                book.setAmountCopiesRemaining(book.getAmountCopiesRemaining() - 1);
                bookDAO.save(book);
            } else {
                throw new ErrorException("El book ingresado no tiene suficientes ejemplares disponibles para realizar el préstamo.");
            }
        } else {
            log.error("El book no esta disponible");
        }
    }

    /**
     * metodo para devolver el book que prestamos
     *
     * @param book
     */
    @Transactional
    @Override
    public void devolutionBook(Book book) {
        if (book != null) {
            book.setAmountCopiesBorrowed(book.getAmountCopiesBorrowed() - 1);
            book.setAmountCopiesRemaining(book.getAmountCopiesRemaining() + 1);
            save(book);
        } else {
            log.error("Error al devolver el book.");
        }
    }

    @Override
    public List<Book> findByAuthor(String id) {
        return bookDAO.findByAuthor(id);
    }

    @Override
    public List<Book> findByPublisher(String id) {
        return bookDAO.findByPublisher(id);
    }

    @Override
    public List<Book> listBookActive() {
        return bookDAO.listBookActive();
    }

    @Override
    public List<Book> listBookInactive() {
        return bookDAO.listBookInactive();
    }

    @Override
    public List<Book> findAll() {
        return bookDAO.findAll();
    }

    @Override
    public Optional<Book> findById(String id) {
        return bookDAO.findById(id);
    }

    @Override
    public void save(Book book) {
        bookDAO.save(book);
    }

    @Override
    public void delete(Book book) {
        bookDAO.delete(book);
    }

    @Override
    public void deleteById(String id) {
        bookDAO.deleteById(id);
    }
}

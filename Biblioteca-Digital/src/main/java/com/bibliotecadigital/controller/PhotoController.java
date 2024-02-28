package com.bibliotecadigital.controller;

import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Book;
import com.bibliotecadigital.entity.Publisher;
import com.bibliotecadigital.entity.User;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.IPublisherService;
import com.bibliotecadigital.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

/**
 * @author Lucas Aramberry
 */
@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublisherService publisherService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public ResponseEntity<byte[]> photoUser(@PathVariable String id) {
        try {
            Optional<User> usuario = userService.findById(id);
            if (usuario.isPresent()) {
                if (usuario.get().getPhoto() == null) {
                    throw new ErrorException("El usuario no tiene una foto asignada.");
                }
            }
            byte[] photo = usuario.get().getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (ErrorException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<byte[]> photoBook(@PathVariable String id) {
        try {
            Optional<Book> libro = bookService.findById(id);
            if (libro.isPresent()) {
                if (libro.get().getPhoto() == null) {
                    throw new ErrorException("El libro no tiene una foto asignada.");
                }
            }
            byte[] photo = libro.get().getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (ErrorException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<byte[]> photoAuthor(@PathVariable String id) {
        try {
            Author author = authorService.findById(id);
            if (author != null) {
                if (author.getPhoto() == null) {
                    throw new ErrorException("El autor no tiene una foto asignada.");
                }
            }
            byte[] photo = author.getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (ErrorException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<byte[]> photoPublisher(@PathVariable String id) {
        try {
            Publisher publisher = publisherService.findById(id);
            if (publisher != null) {
                if (publisher.getPhoto() == null) {
                    throw new ErrorException("El editorial no tiene una foto asignada.");
                }
            }
            byte[] photo = publisher.getPhoto().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(photo, headers, HttpStatus.OK);
        } catch (ErrorException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

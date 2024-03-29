package com.bibliotecadigital.controllers;

import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.services.IAuthorService;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.IPublisherService;
import com.bibliotecadigital.services.IUserService;
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
            User user = userService.findById(id);

            if (user.getPhoto() == null) {
                throw new ErrorException("The user does not have a photo assigned.");
            }

            byte[] photo = user.getPhoto().getContent();

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
            Book book = bookService.findById(id);
            if (book.getPhoto() == null) {
                throw new ErrorException("The book does not have a photo assigned.");
            }

            byte[] photo = book.getPhoto().getContent();

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
            if (author.getPhoto() == null) {
                throw new ErrorException("The author does not have a photo assigned.");
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
            if (publisher.getPhoto() == null) {
                throw new ErrorException("The publisher does not have a photo assigned.");
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

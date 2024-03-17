package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.*;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.services.IAuthorService;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.IPublisherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private IBookService bookService;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublisherService publisherService;

    @GetMapping
    public String books(ModelMap model, HttpSession session, @RequestParam(required = false) String idBook) {

        try {
            List<Book> booksActive;

            User userLogin = (User) session.getAttribute("usersession");
            if (userLogin != null && userLogin.getRole().equals(Role.ADMIN)) {
                booksActive = bookService.findAll();
            } else {
                booksActive = bookService.listBookActive();
            }

            model.put("booksActive", booksActive);
            model.addAttribute("books", booksActive);

            if (idBook != null) {
                model.addAttribute("books", bookService.findById(idBook));
            }

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "libros.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/view/{id}")
    public String viewBooks(@PathVariable String id, ModelMap model) {

        try {

            model.addAttribute("loan", LoanDto.builder().build());

            Book book = bookService.findById(id);

            model.addAttribute("book", BookDto
                    .builder()
                    .id(book.getId())
                    .isbn(book.getIsbn())
                    .title(book.getTitle())
                    .description(book.getDescription())
                    .datePublisher(book.getDatePublisher())
                    .amountPages(book.getAmountPages())
                    .amountCopies(book.getAmountCopies())
                    .amountCopiesBorrowed(book.getAmountCopiesBorrowed())
                    .amountCopiesRemaining(book.getAmountCopiesRemaining())
                    .photoDto(PhotoDto.builder().build())
                    .idAuthor(book.getAuthor().getId())
                    .idPublisher(book.getPublisher().getId())
                    .register(book.getRegister())
                    .unsubscribe(book.getUnsubscribe())
                    .build()
            );

            Author author = authorService.findById(book.getAuthor().getId());

            model.addAttribute("author", AuthorDto
                    .builder()
                    .id(author.getId())
                    .name(author.getName())
                    .build()
            );

            Publisher publisher = publisherService.findById(book.getPublisher().getId());

            model.addAttribute("publisher", PublisherDto
                    .builder()
                    .id(book.getPublisher().getId())
                    .name(book.getPublisher().getName())
                    .build()
            );

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "libro.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/register")
    public String registerBook(ModelMap model) {

        List<Author> authors = authorService.findAll();
        List<Publisher> publishers = publisherService.findAll();

        model.addAttribute("book", BookDto.builder().build());
        model.put("authors", authors);
        model.put("publishers", publishers);

        return "registro-libro.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public String registerBook(ModelMap model, @ModelAttribute(name = "book") @Valid BookDto bookDto, BindingResult result) {

        if (bookDto.getAmountCopies() < bookDto.getAmountCopiesBorrowed()) result.addError(new FieldError("bookDto", "amountCopies", "Amount copies cannot be less amount copies borrowed"));

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            List<Author> authors = authorService.findAll();
            List<Publisher> publishers = publisherService.findAll();
            model.put("authors", authors);
            model.put("publishers", publishers);
            return "registro-libro.html";
        }

        try {
            bookService.register(bookDto);
            model.put("titulo", "Registro exitoso!");
            model.put("descripcion", "El libro ingresado fue registrado correctamente.");

            return "exito.html";

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String updateBook(ModelMap model, @PathVariable String id) {

        try {
            model.put("authors", authorService.findAll());
            model.put("publishers", publisherService.findAll());

            Book book = null;
            book = bookService.findById(id);

            model.addAttribute("book", BookDto
                            .builder()
                            .id(book.getId())
                            .isbn(book.getIsbn())
                            .title(book.getTitle())
                            .description(book.getDescription())
                            .datePublisher(book.getDatePublisher())
                            .amountPages(book.getAmountPages())
                            .amountCopies(book.getAmountCopies())
                            .amountCopiesBorrowed(book.getAmountCopiesBorrowed())
                            .amountCopiesRemaining(book.getAmountCopiesRemaining())
                            .photoDto(PhotoDto
                                            .builder()
//                        .file((MultipartFile) book.getPhoto())
                                            .build()
                            )
                            .idAuthor(book.getAuthor().getId())
                            .idPublisher(book.getPublisher().getId())
                            .build()
            );

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "modificar-libro.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateBook(ModelMap model, @ModelAttribute(name = "book") @Valid BookDto bookDto, BindingResult result) {

        if (bookDto.getAmountCopies() < bookDto.getAmountCopiesBorrowed()) result.addError(new FieldError("bookDto", "amountCopies", "Amount copies cannot be less amount copies borrowed"));

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            model.put("authors", authorService.findAll());
            model.put("publishers", publisherService.findAll());
            return "modificar-libro.html";
        }

        try {
            bookService.update(bookDto);
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable String id) {
        try {
            bookService.low(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable String id) {
        try {
            bookService.high(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable String id) {
        try {
            bookService.delete(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/book";
    }
}

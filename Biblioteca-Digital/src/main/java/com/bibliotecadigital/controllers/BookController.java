package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.IPublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
    public String books(ModelMap model, @RequestParam(required = false) String idBook, @RequestParam(required = false) String idAuthor, @RequestParam(required = false) String idPublisher) {

        List<Book> booksActive = bookService.listBookActive();

        model.put("booksActive", booksActive);
        model.addAttribute("books", booksActive);

        if (idBook != null) {
            Optional<Book> bookForTitle = bookService.findById(idBook);
            model.addAttribute("books", bookForTitle.isPresent() ? bookForTitle.get() : booksActive);
        }

        return "libros.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/view/{id}")
    public String viewBooks(@PathVariable String id, ModelMap model) {

        model.addAttribute("loan", LoanDto.builder().build());

        Optional<Book> book = bookService.findById(id);

        model.addAttribute("book", BookDto
                .builder()
                .id(book.get().getId())
                .isbn(book.get().getIsbn())
                .title(book.get().getTitle())
                .description(book.get().getDescription())
                .datePublisher(book.get().getDatePublisher())
                .amountPages(book.get().getAmountPages())
                .amountCopies(book.get().getAmountCopies())
                .amountCopiesBorrowed(book.get().getAmountCopiesBorrowed())
                .amountCopiesRemaining(book.get().getAmountCopiesRemaining())
                .photoDto(PhotoDto
                        .builder()
                        .file((MultipartFile) book.get().getPhoto())
                        .build()
                )
                .idAuthor(book.get().getAuthor().getId())
                .idPublisher(book.get().getPublisher().getId())
                .build()
        );

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

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            List<Author> authors = authorService.findAll();
            List<Publisher> publishers = publisherService.findAll();
            model.put("authors", authors);
            model.put("publishers", publishers);
            return "registro-libro.html";
        }

        bookService.register(bookDto);

        model.put("titulo", "Registro exitoso!");
        model.put("descripcion", "El libro ingresado fue registrado correctamente.");

        return "exito.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String updateBook(ModelMap model, @PathVariable String id) {

        model.put("authors", authorService.findAll());
        model.put("publishers", publisherService.findAll());

        Optional<Book> book = bookService.findById(id);

        model.addAttribute("book", BookDto
                .builder()
                .id(book.get().getId())
                .isbn(book.get().getIsbn())
                .title(book.get().getTitle())
                .description(book.get().getDescription())
                .datePublisher(book.get().getDatePublisher())
                .amountPages(book.get().getAmountPages())
                .amountCopies(book.get().getAmountCopies())
                .amountCopiesBorrowed(book.get().getAmountCopiesBorrowed())
                .amountCopiesRemaining(book.get().getAmountCopiesRemaining())
                .photoDto(PhotoDto
                        .builder()
                        .file((MultipartFile) book.get().getPhoto())
                        .build()
                )
                .idAuthor(book.get().getAuthor().getId())
                .idPublisher(book.get().getPublisher().getId())
                .build()
        );

        return "modificar-libro.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateBook(ModelMap model, @ModelAttribute(name = "book") @Valid BookDto bookDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            model.put("authors", authorService.findAll());
            model.put("publishers", publisherService.findAll());
            return "modificar-libro.html";
        }

        bookService.update(bookDto.getId(), bookDto);

        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable String id) {
        bookService.low(id);
        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable String id) {
        bookService.high(id);
        return "redirect:/book";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable String id) {
        bookService.delete(id);
        return "redirect:/book";
    }
}

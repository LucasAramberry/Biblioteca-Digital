package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Book;
import com.bibliotecadigital.entity.Publisher;
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

        List<Book> bookActive = bookService.listBookActive();
        model.put("bookActive", bookActive);

        List<Book> books = bookService.listBookInactive();
        model.addAttribute("books", books);

        if (idBook != null) {
            Optional<Book> bookForTitle = bookService.findById(idBook);
            model.addAttribute("books", bookForTitle.get());
        }

        return "libros.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/view/{id}")

    public String viewBooks(@PathVariable String id, ModelMap model) {
        //pasamos la fecha actual por si qremos realizar el prestamo
        model.addAttribute("loan", LoanDto.builder().build());

        model.put("book", bookService.findById(id));

        return "libro.html";
    }

    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/register")
    public String registerBook(ModelMap model) {

        List<Author> authors = authorService.findAll();
        List<Publisher> publishers = publisherService.findAll();

        model.addAttribute("book", BookDto.builder().build());
        model.put("authors", authors);
        model.put("publishers", publishers);

        return "registro-libro.html";
    }

    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/update")
    public String updateBook(ModelMap model, @RequestParam String id) {

        List<Author> authors = authorService.findAll();
        model.put("authors", authors);

        List<Publisher> publishers = publisherService.findAll();
        model.put("publishers", publishers);

        model.addAttribute("book", bookService.findById(id));

        return "modificar-libro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateBook(ModelMap model, @RequestParam String id, @ModelAttribute(name = "book") @Valid BookDto bookDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            List<Author> authors = authorService.findAll();
            List<Publisher> publishers = publisherService.findAll();
            model.put("authors", authors);
            model.put("publishers", publishers);
            return "modificar-libro.html";
        }

        bookService.update(id, bookDto);
        return "redirect:/book";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/low")
    public String low(ModelMap model, @RequestParam String id) {
        bookService.low(id);
        return "redirect:/book";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/high")
    public String high(ModelMap model, @RequestParam String id) {
        bookService.high(id);
        return "redirect:/book";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(ModelMap model, @RequestParam String id) {
        bookService.delete(id);
        return "redirect:/book";
    }
}

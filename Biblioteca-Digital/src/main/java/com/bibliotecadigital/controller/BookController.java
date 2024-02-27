package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.BookDto;
import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entity.Author;
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

/**
 * @author Lucas Aramberry
 */
//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
@Controller
@RequestMapping("/libros")
public class BookController {

    @Autowired
    private IBookService bookService;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublisherService publisherService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/mostrar/{id}")
    public String viewBooks(@PathVariable String id, ModelMap modelo) {
        //pasamos la fecha actual por si qremos realizar el prestamo
        modelo.addAttribute("prestamo", LoanDto.builder().build());

        modelo.put("libro", bookService.findById(id));

        return "libro.html";
    }

    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registro")
    public String registerBook(ModelMap modelo) {

        List<Author> autores = authorService.findAll();
        List<Publisher> editoriales = publisherService.findAll();

        modelo.addAttribute("libro", BookDto.builder().build());
        modelo.put("autores", autores);
        modelo.put("editoriales", editoriales);

        return "registro-libro.html";
    }

    //    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/registrar")
    public String registerBook(ModelMap modelo, @ModelAttribute(name = "libro") @Valid BookDto bookDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("libro", bookDto);
            List<Author> autores = authorService.findAll();
            List<Publisher> editoriales = publisherService.findAll();
            modelo.put("autores", autores);
            modelo.put("editoriales", editoriales);
            return "registro-libro.html";
        }

        bookService.register(bookDto);

        modelo.put("titulo", "Registro exitoso!");
        modelo.put("descripcion", "El libro ingresado fue registrado correctamente.");

        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar")
    public String updateBook(ModelMap modelo, @RequestParam String id) {

        List<Author> autores = authorService.findAll();
        modelo.put("autores", autores);

        List<Publisher> editoriales = publisherService.findAll();
        modelo.put("editoriales", editoriales);

        modelo.addAttribute("libro", bookService.findById(id));

        return "modificar-libro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar")
    public String modificarLibro(ModelMap modelo, @RequestParam String id, @ModelAttribute(name = "libro") @Valid BookDto bookDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("libro", bookDto);
            List<Author> autores = authorService.findAll();
            List<Publisher> editoriales = publisherService.findAll();
            modelo.put("autores", autores);
            modelo.put("editoriales", editoriales);
            return "modificar-libro.html";
        }

        bookService.update(id, bookDto);
        return "redirect:/libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/baja")
    public String low(ModelMap modelo, @RequestParam String id) {
        bookService.low(id);
        return "redirect:/libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta")
    public String high(ModelMap modelo, @RequestParam String id) {
        bookService.high(id);
        return "redirect:/libros";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar")
    public String delete(ModelMap modelo, @RequestParam String id) {
        bookService.delete(id);
        return "redirect:/libros";
    }
}

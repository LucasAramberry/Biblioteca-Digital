package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Book;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IBookService bookService;

    @GetMapping
    public String authors(ModelMap model, @RequestParam(required = false) String idAuthor) {

        List<Author> listActive = authorService.findByActive();
        model.addAttribute("authorsActive", listActive);

        List<Author> authors = authorService.findByActive();
        model.addAttribute("authors", authors);

        if (idAuthor != null) {
            List<Book> books = bookService.findByAuthor(idAuthor);
            model.addAttribute("books", books);

            model.addAttribute("authors", authorService.findById(idAuthor));
        }

        return "autores.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/register")
    public String registerAuthor(ModelMap model) {

        model.addAttribute("author", new AuthorDto());

        return "registro-autor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public String registerAuthor(ModelMap model, @ModelAttribute(name = "author") @Valid AuthorDto authorDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("author", authorDto);
            return "registro-autor.html";
        }

        authorService.register(authorDto);

        model.put("titulo", "Registro exitoso!");
        model.put("descripcion", "El autor ingresado fue registrado correctamente.");

        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/update")
    public String updateAuthor(ModelMap model, @RequestParam String id) {

        Author author = authorService.findById(id);

        if (author != null) {
            AuthorDto authorDto = AuthorDto
                    .builder()
                    .id(author.getId())
                    .name(author.getName())
                    .photoDto(PhotoDto
                            .builder()
                            .file((MultipartFile) author.getPhoto())
                            .build())
                    .build();

            model.addAttribute("author", authorDto);

            return "modificar-autor.html";
        }

        return "redirect:/author";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateAuthor(ModelMap model, @ModelAttribute(name = "author") @Valid AuthorDto authorDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("author", authorDto);
            return "modificar-autor.html";
        }

        authorService.update(authorDto.getId(), authorDto);
        return "redirect:/author";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/low")
    public String low(ModelMap model, @RequestParam String id) {

        authorService.low(id);
        return "redirect:/author";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/high")
    public String high(ModelMap model, @RequestParam String id) {

        authorService.high(id);
        return "redirect:/author";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(ModelMap model, @RequestParam String id) {

        authorService.delete(id);
        return "redirect:/author";

    }
}

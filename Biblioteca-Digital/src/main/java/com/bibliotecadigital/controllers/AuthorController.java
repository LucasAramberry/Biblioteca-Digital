package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.services.IAuthorService;
import com.bibliotecadigital.services.IBookService;
import jakarta.servlet.http.HttpSession;
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
@Controller
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IBookService bookService;

    @GetMapping
    public String authors(ModelMap model, HttpSession session, @RequestParam(required = false) String idAuthor) {

        try {

            List<Author> authorsList;

            User userLogin = (User) session.getAttribute("usersession");
            if (userLogin != null && userLogin.getRole().equals(Role.ADMIN)) {
                authorsList = authorService.findAll();
            } else {
                authorsList = authorService.findByActive();
            }

            model.addAttribute("authorsList", authorsList);

            if (idAuthor != null) {
                model.addAttribute("books", bookService.findByAuthor(idAuthor));
                model.addAttribute("authors", idAuthor.equalsIgnoreCase("todos") ? authorsList : authorService.findById(idAuthor));
            } else {
                model.addAttribute("authors", authorsList.stream().toList());
            }

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "autores.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/register")
    public String registerAuthor(ModelMap model) {

        model.addAttribute("author", new AuthorDto());

        return "registro-autor.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String updateAuthor(ModelMap model, @PathVariable String id) {

        try {
            Author author = authorService.findById(id);

            model.addAttribute("author", AuthorDto
                    .builder()
                    .id(author.getId())
                    .name(author.getName())
                    .photoDto(PhotoDto.builder().build())
                    .build()
            );

            return "modificar-autor.html";

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/author";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateAuthor(ModelMap model, @ModelAttribute(name = "author") @Valid AuthorDto authorDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("author", authorDto);
            return "modificar-autor.html";
        }

        try {
            authorService.update(authorDto);
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/author";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable String id) {

        try {
            authorService.low(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/author";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable String id) {

        try {
            authorService.high(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/author";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable String id) {

        try {
            authorService.delete(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/author";

    }
}

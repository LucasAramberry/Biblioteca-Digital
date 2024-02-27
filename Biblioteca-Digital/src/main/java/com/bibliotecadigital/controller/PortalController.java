package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entity.*;
import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.service.*;
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
@RequestMapping("/")
public class PortalController {

    @Autowired
    private ICityService cityService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IAuthorService authorService;
    @Autowired
    private IPublisherService publisherService;
    @Autowired
    private ILoanService loanService;

    @GetMapping("/")
    public String index(ModelMap modelo) {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {
        if (error != null) {
            model.put("error", "Email o contraseña incorrectos.");
        }
        if (logout != null) {
            model.put("logout", "Ha salido correctamente de la plataforma.");
        }

        return "login.html";
    }

    @GetMapping("/registro")
    public String register(ModelMap modelo) {
        List<City> ciudad = cityService.findAll();
        modelo.put("zonas", ciudad);
        modelo.put("sexos", Gender.values());

        modelo.addAttribute("usuario", UserDto.builder().build());

        return "registro.html";
    }

    @PostMapping("/registrar")
    public String register(ModelMap modelo, @ModelAttribute(name = "usuario") @Valid UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("usuario", userDto);
            List<City> ciudad = cityService.findAll();
            modelo.put("zonas", ciudad);
            modelo.put("sexos", Gender.values());
            return "registro.html";
        }

        userService.register(userDto);

        modelo.put("titulo", "¡Bienvenido a la Libreria!");
        modelo.put("descripcion", "Tu usuario fue registrado de manera satisfactoria.");
        return "exito.html";
    }

    @GetMapping("/libros")
    public String books(ModelMap modelo, @RequestParam(required = false) String idBook, @RequestParam(required = false) String idAuthor, @RequestParam(required = false) String idPublisher) {
        List<Book> librosActivos = bookService.listBookActive();
        modelo.put("librosA", librosActivos);

        List<Book> libros = bookService.listBookInactive();
        modelo.addAttribute("libros", libros);

        if (idBook != null) {
            Optional<Book> libroPorTitulo = bookService.findById(idBook);
            modelo.addAttribute("libros", libroPorTitulo.get());
        }
        return "libros.html";
    }

    @GetMapping("/editoriales")
    public String editoriales(ModelMap modelo, @RequestParam(required = false) String idPublisher) {

        List<Publisher> listaPublisheres = publisherService.findAll();
        modelo.addAttribute("editoriales", listaPublisheres);

        List<Publisher> listaActivos = publisherService.findByActive();
        modelo.addAttribute("editorialesA", listaActivos);

        if (idPublisher != null) {
            List<Book> libros = bookService.findByPublisher(idPublisher);
            modelo.addAttribute("libros", libros);

            Optional<Publisher> editorial = publisherService.findById(idPublisher);
            modelo.addAttribute("editoriales", editorial.get());
        }

        return "editoriales.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/prestamos")
    public String prestamos(ModelMap model, @RequestParam(required = false) String idUser) {
        List<User> listaActivos = userService.findAll();
        model.addAttribute("usuarios", listaActivos);

        model.addAttribute("usuarioSelected", null);

        if (idUser != null) {
            List<Loan> prestamosUser = loanService.findByUser(idUser);
            model.put("prestamos", prestamosUser);
            model.addAttribute("usuarioSelected", userService.findById(idUser));
        } else {
            List<Loan> prestamos = loanService.findAll();
            model.put("prestamos", prestamos);
        }
        return "prestamos.html";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/usuarios")
    public String usuarios(ModelMap model, @RequestParam(required = false) String idUser) {
        model.put("rolAdmin", Role.ADMIN);
        model.put("rolUser", Role.USER);

        List<User> listaBuscarUsers = userService.findAll();
        model.addAttribute("usuariosLista", listaBuscarUsers);

        List<User> listaUsers = userService.findAll();
        model.addAttribute("usuarios", listaUsers);

        model.addAttribute("usuarioSelected", null);

        if (idUser != null) {
            Optional<User> usuario;
            usuario = userService.findById(idUser);
            model.put("usuarios", usuario.get());
            model.addAttribute("usuarioSelected", usuario);
        }
        return "usuarios.html";
    }
}

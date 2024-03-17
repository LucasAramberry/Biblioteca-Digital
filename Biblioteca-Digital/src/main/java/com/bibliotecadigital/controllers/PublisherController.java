package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.IPublisherService;
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
@RequestMapping("/publisher")
public class PublisherController {

    @Autowired
    private IPublisherService publisherService;
    @Autowired
    private IBookService bookService;

    @GetMapping
    public String publishers(ModelMap model, HttpSession session, @RequestParam(required = false) String idPublisher) {

        try {
            List<Publisher> publisherList;

            User userLogin = (User) session.getAttribute("usersession");
            if (userLogin != null && userLogin.getRole().equals(Role.ADMIN)) {
                publisherList = publisherService.findAll();
            } else {
                publisherList = publisherService.findByActive();
            }

            model.addAttribute("publisherList", publisherList);

            if (idPublisher != null) {
                model.addAttribute("books", bookService.findByPublisher(idPublisher));
                model.addAttribute("publishers", publisherService.findById(idPublisher));
            } else {
                model.addAttribute("publishers", publisherList.stream().toList());
            }

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "editoriales.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/register")
    public String registerPublisher(ModelMap model) {
        model.addAttribute("publisher", PublisherDto.builder().build());
        return "registro-editorial.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public String registerPublisher(ModelMap model, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("publisher", publisherDto);
            return "registro-editorial.html";
        }

        publisherService.register(publisherDto);

        model.put("titulo", "Registro exitoso!");
        model.put("descripcion", "La editorial ingresada fue registrado correctamente.");

        return "exito.html";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/update/{id}")
    public String updatePublisher(ModelMap model, @PathVariable String id) {

        try {
            Publisher publisher = null;
            publisher = publisherService.findById(id);

            if (publisher != null) {
                PublisherDto publisherDto = PublisherDto
                        .builder()
                        .id(publisher.getId())
                        .name(publisher.getName())
                        .photoDto(PhotoDto.builder().build())
                        .build();

                model.addAttribute("publisher", publisherDto);

                return "modificar-editorial.html";
            }
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/publisher";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updatePublisher(ModelMap model, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        try {
            if (result.hasErrors()) {
                model.addAttribute("publisher", publisherDto);
                return "modificar-editorial.html";
            }

            publisherService.update(publisherDto.getId(), publisherDto);

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/publisher";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable String id) {
        try {
            publisherService.low(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/publisher";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable String id) {
        try {
            publisherService.high(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/publisher";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable String id) {
        try {
            publisherService.delete(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/publisher";
    }
}

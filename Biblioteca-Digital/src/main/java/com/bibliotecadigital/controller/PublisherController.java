package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entity.Publisher;
import com.bibliotecadigital.service.IPublisherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Lucas Aramberry
 */
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/editoriales")
public class PublisherController {

    @Autowired
    private IPublisherService publisherService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/registro")
    public String registroPublisher(ModelMap modelo) {
        modelo.addAttribute("publisher", PublisherDto.builder().build());
        return "registro-editorial.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public String registerPublisher(ModelMap modelo, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("publisher", publisherDto);
            return "registro-editorial.html";
        }

        publisherService.register(publisherDto);

        modelo.put("titulo", "Registro exitoso!");
        modelo.put("descripcion", "La editorial ingresado fue registrado correctamente.");
        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar")
    public String modificarPublisher(ModelMap modelo, @RequestParam String id) {

        Optional<Publisher> editorial = publisherService.findById(id);
        modelo.addAttribute("editorial", editorial.get());

        return "modificar-editorial.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar")
    public String modificarPublisher(ModelMap modelo, @RequestParam String id, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("editorial", publisherDto);
            return "modificar-editorial.html";
        }

        Optional<Publisher> editorial = editorial = publisherService.findById(id);
        publisherService.update(id, publisherDto);
        return "redirect:/editoriales";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/baja")
    public String baja(ModelMap modelo, @RequestParam String id) {
        publisherService.low(id);
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta")
    public String alta(ModelMap modelo, @RequestParam String id) {
        publisherService.high(id);
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar")
    public String eliminar(ModelMap modelo, @RequestParam String id) {
        publisherService.delete(id);
        return "redirect:/editoriales";
    }
}

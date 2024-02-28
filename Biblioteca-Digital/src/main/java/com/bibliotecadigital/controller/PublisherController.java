package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entity.Book;
import com.bibliotecadigital.entity.Publisher;
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
    public String publishers(ModelMap model, @RequestParam(required = false) String idPublisher) {

        List<Publisher> listActive = publisherService.findByActive();
        model.addAttribute("publisherActive", listActive);

        List<Publisher> publishers = publisherService.findAll();
        model.addAttribute("publishers", publishers);

        if (idPublisher != null) {
            List<Book> books = bookService.findByPublisher(idPublisher);
            model.addAttribute("books", books);

            model.addAttribute("editoriales", publisherService.findById(idPublisher));
        }

        return "editoriales.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/register")
    public String registerPublisher(ModelMap model) {
        model.addAttribute("publisher", PublisherDto.builder().build());
        return "registro-editorial.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/register")
    public String registerPublisher(ModelMap model, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("publisher", publisherDto);
            return "registro-editorial.html";
        }

        publisherService.register(publisherDto);

        model.put("titulo", "Registro exitoso!");
        model.put("descripcion", "La editorial ingresado fue registrado correctamente.");

        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/update")
    public String updatePublisher(ModelMap model, @RequestParam String id) {

        Publisher publisher = publisherService.findById(id);

        if (publisher != null) {
            PublisherDto publisherDto = PublisherDto
                    .builder()
                    .id(publisher.getId())
                    .name(publisher.getName())
                    .photoDto(PhotoDto
                            .builder()
                            .file((MultipartFile) publisher.getPhoto())
                            .build())
                    .build();

            model.addAttribute("publisher", publisherDto);

            return "modificar-editorial.html";
        }

        return "redirect:/publisher";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updatePublisher(ModelMap model, @ModelAttribute(name = "publisher") @Valid PublisherDto publisherDto, BindingResult result) {

        if (result.hasErrors()) {
            model.addAttribute("publisher", publisherDto);
            return "modificar-editorial.html";
        }

        publisherService.update(publisherDto.getId(), publisherDto);
        return "redirect:/publisher";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/low")
    public String low(ModelMap model, @RequestParam String id) {

        publisherService.low(id);
        return "redirect:/publisher";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/high")
    public String high(ModelMap model, @RequestParam String id) {

        publisherService.high(id);
        return "redirect:/publisher";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(ModelMap model, @RequestParam String id) {

        publisherService.delete(id);
        return "redirect:/publisher";

    }
}

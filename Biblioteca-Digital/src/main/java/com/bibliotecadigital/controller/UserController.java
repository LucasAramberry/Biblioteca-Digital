package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entity.City;
import com.bibliotecadigital.entity.User;
import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.service.ICityService;
import com.bibliotecadigital.service.IUserService;
import jakarta.servlet.http.HttpSession;
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
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@Controller
@RequestMapping("/usuario")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICityService cityService;

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/editar-perfil")
    public String editarPerfil(HttpSession session, ModelMap modelo, @RequestParam String id) {

        List<City> zonas = cityService.findAll();
        modelo.put("zonas", zonas);

        modelo.put("sexos", Gender.values());

        User login = (User) session.getAttribute("usuariosession");
        if (login == null || !login.getId().equals(id)) {
            return "redirect:/";
        }

        Optional<User> usuario = userService.findById(id);
        modelo.addAttribute("usuario", usuario.get());

        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/actualizar-perfil")
    public String actualizarPerfil(ModelMap modelo, HttpSession session, @ModelAttribute(name = "user") @Valid UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {
            modelo.addAttribute("user", userDto);
            List<City> zonas = cityService.findAll();

            modelo.put("zonas", zonas);
            modelo.put("sexos", Gender.values());

            return "perfil.html";
        }

        User login = (User) session.getAttribute("usuariosession");
        if (login == null) {// || !login.getId().equals(id)
            return "redirect:/";
        }

        Optional<User> usuario = userService.findById(login.getId());
        userService.update(login.getId(), userDto);
        session.setAttribute("usuariosession", usuario.get());
        return "redirect:/inicio";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/cambiar-rol")
    public String cambiarRol(ModelMap modelo, @RequestParam String id) {
        userService.changeRol(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/baja")
    public String baja(ModelMap modelo, @RequestParam String id) {
        userService.low(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta")
    public String alta(ModelMap modelo, @RequestParam String id) {
        userService.high(id);
        return "redirect:/usuarios";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar")
    public String eliminar(ModelMap modelo, @RequestParam String id) {
        userService.delete(id);
        return "redirect:/usuarios";
    }
}

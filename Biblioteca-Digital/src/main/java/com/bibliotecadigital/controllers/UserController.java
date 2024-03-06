package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.service.ICityService;
import com.bibliotecadigital.service.IUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ICityService cityService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String users(ModelMap model, @RequestParam(required = false) String idUser) {

        try {
            List<User> userList = userService.findAll();
            model.addAttribute("userList", userList);

            if (idUser != null) {
                model.put("users", (idUser.equalsIgnoreCase("todos")) ? userList : userService.findById(idUser));
            }
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "usuarios.html";
    }

    @GetMapping("/register")
    public String register(ModelMap model) {

        model.put("cities", cityService.findAll());
        model.put("genders", Gender.values());

        model.addAttribute("user", UserDto.builder().build());

        return "registro.html";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute(name = "user") @Valid UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {

            model.addAttribute("user", userDto);
            model.addAttribute("cities", cityService.findAll());
            model.addAttribute("genders", Gender.values());

            return "registro.html";
        }

        try {
            userService.register(userDto);

            model.addAttribute("titulo", "¡Bienvenido a Biblioteca Digital!");
            model.addAttribute("descripcion", "Tu usuario fue registrado de manera satisfactoria.");
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "exito.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/edit-profile/{id}")
    public String editProfile(HttpSession session, ModelMap model, @PathVariable String id) {

        User login = (User) session.getAttribute("usersession");

        if (login == null || !login.getId().equals(id)) {
            return "redirect:/";
        }

        model.put("cities", cityService.findAll());

        model.put("genders", Gender.values());

        model.addAttribute("user", UserDto
                .builder()
                .id(login.getId())
                .name(login.getName())
                .lastName(login.getLastName())
                .dni(login.getDni())
                .gender(login.getGender())
                .phone(login.getPhone())
                .role(login.getRole())
                .idCity(login.getCity().getId())
                .email(login.getEmail())
                .password(login.getPassword())
                .photoDto(PhotoDto.builder().build())
                .build());

        return "perfil.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/update-profile")
    public String updateProfile(ModelMap model, HttpSession session, @ModelAttribute(name = "user") @Valid UserDto userDto, BindingResult result) {

        if (result.hasErrors()) {

            model.addAttribute("user", userDto);
            model.put("cities", cityService.findAll());
            model.put("genders", Gender.values());

            return "perfil.html";
        }

        try {
            User login = (User) session.getAttribute("usersession");
            if (login == null) {
                return "redirect:/";
            }

            userService.update(userDto);

            User user = userService.findById(login.getId());
            session.setAttribute("usersession", user);

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/change-role/{id}")
    public String changeRole(ModelMap model, @PathVariable String id) {
        try {
            userService.changeRol(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable String id) {
        try {
            userService.low(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable String id) {
        try {
            userService.high(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/user";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable String id) {
        try {
            userService.delete(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/user";
    }

}

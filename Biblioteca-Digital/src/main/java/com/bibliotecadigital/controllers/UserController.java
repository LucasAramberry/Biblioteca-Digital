package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.enums.Role;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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

        userService.register(userDto);

        model.addAttribute("titulo", "¡Bienvenido a Biblioteca Digital!");
        model.addAttribute("descripcion", "Tu usuario fue registrado de manera satisfactoria.");

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
                .name(login.getName())
                .lastName(login.getLastName())
                .dni(login.getDni())
                .gender(login.getGender())
                .phone(login.getPhone())
                .role(login.getRole())
                .idCity(login.getCity().getId())
                .email(login.getEmail())
                .password(login.getPassword())
                .photoDto(PhotoDto
                        .builder()
                        .file((MultipartFile) login.getPhoto())
                        .build()
                )
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

        User login = (User) session.getAttribute("usersession");
        if (login == null) {// || !login.getId().equals(id)
            return "redirect:/";
        }

        userService.update(login.getId(), userDto);

        Optional<User> user = userService.findById(login.getId());
        session.setAttribute("usersession", user.get());

        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/change-role")
    public String changeRole(ModelMap model, @RequestParam String id) {
        userService.changeRol(id);
        return "redirect:/user";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/low")
    public String low(ModelMap model, @RequestParam String id) {
        userService.low(id);
        return "redirect:/user";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/high")
    public String high(ModelMap model, @RequestParam String id) {
        userService.high(id);
        return "redirect:/user";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(ModelMap model, @RequestParam String id) {
        userService.delete(id);
        return "redirect:/user";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/users")
    public String users(ModelMap model, @RequestParam(required = false) String idUser) {
        model.put("rolAdmin", Role.ADMIN);
        model.put("rolUser", Role.USER);

        List<User> listUsers = userService.findAll();
        model.addAttribute("userList", listUsers);

        List<User> users = userService.findAll();
        model.addAttribute("users", users);

        model.addAttribute("userSelected", null);

        if (idUser != null) {
            Optional<User> user = userService.findById(idUser);
            model.put("users", user.get());
            model.addAttribute("userSelected", user);
        }
        return "usuarios.html";
    }
}

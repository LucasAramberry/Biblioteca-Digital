package com.bibliotecadigital.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lucas Aramberry
 */
@Controller
@RequestMapping("/")
public class PortalController {

    @GetMapping("/")
    public String index(ModelMap model) {
        return "index.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap model) {

        if (error != null) {
            model.put("error", "Email o contraseña incorrectos.");
        }
        if (logout != null) {
            model.put("logout", "Ha cerrado sesión correctamente.");
        }

        return "login.html";
    }

}
package com.bibliotecadigital.controller;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entity.*;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.ILoanService;
import com.bibliotecadigital.service.IUserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author Lucas
 */
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@Controller
@RequestMapping("/prestamos")
public class LoanController {

    @Autowired
    private ILoanService loanService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/mis-prestamos")
    public String myLoans(HttpSession session, ModelMap model) {

        User login = (User) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/";
        }
        List<Loan> misPrestamos = loanService.findByUser(login.getId());
        model.put("prestamos", misPrestamos);

        return "prestamos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/registro")
    public String registerLoan(HttpSession session, ModelMap model) {

        User login = (User) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/inicio";
        }

        //Verificamos si es un administrador para pasarle la lista de usuarios
        if (login.getRole().equals(Role.ADMIN)) {
            List<User> usuarios = userService.findAll();
            model.put("usuarios", usuarios);
        }

        List<Book> libros = bookService.findAll();
        model.put("libros", libros);

        model.addAttribute("prestamo", LoanDto.builder().build());

        return "registro-prestamo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/registrar")
    public String registerLoan(ModelMap modelo, HttpSession session, @ModelAttribute(name = "prestamo") @Valid LoanDto loanDto, BindingResult result) throws ErrorException {

        //Usamos el usuario logueado para obtener el id de la sesion del usuario y
        //no mandarlo a travez del input hidden del html
        User login = (User) session.getAttribute("usuariosession");
        if (login == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            modelo.addAttribute("prestamo", loanDto);
            return "redirect:/";
        }

        //verificamos si es usuario y sacamos el id de la sesion y si es admin mandamos lista de usuarios
        if (login.getRole().equals(Role.USER)) {
            //Si buscamos el usuario asi tenemos q habilitar el input hidden del html y
            //colocar el atributo como requerido y comentar el login ya q no haria falta

            loanService.register(loanDto);

            return "redirect:/prestamos/mis-prestamos"; //cambiar return
        } else {
            loanService.register(loanDto);
            return "redirect:/prestamos";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/modificar")
    public String updateLoan(ModelMap modelo, @RequestParam Long id) {

        List<Book> libros = bookService.findAll();
        modelo.put("libros", libros);

        List<User> usuarios = userService.findAll();
        modelo.put("usuarios", usuarios);

        Optional<Loan> prestamo = loanService.findById(id);
        modelo.addAttribute("prestamo", prestamo.get());

        return "modificar-prestamo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/actualizar")
    public String updateLoan(ModelMap modelo, @RequestParam Long id, @ModelAttribute(name = "prestamo") @Valid LoanDto loanDto, BindingResult result) throws ErrorException {

        if (result.hasErrors()) {
            List<Book> libros = bookService.findAll();
            modelo.put("libros", libros);

            List<User> usuarios = userService.findAll();
            modelo.put("usuarios", usuarios);

            modelo.addAttribute("prestamo", loanDto);
            return "modificar-prestamo.html";
        }
        loanService.update(id, loanDto);
        return "redirect:/prestamos";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/baja")
    public String low(ModelMap modelo, @RequestParam Long id) {
        loanService.low(id);
        return "redirect:/prestamos";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/alta")
    public String high(ModelMap modelo, @RequestParam Long id) throws ErrorException {
        loanService.high(id);
        return "redirect:/prestamos";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/eliminar")
    public String delete(ModelMap modelo, @RequestParam Long id) {
        loanService.delete(id);
        return "redirect:/prestamos";
    }
}

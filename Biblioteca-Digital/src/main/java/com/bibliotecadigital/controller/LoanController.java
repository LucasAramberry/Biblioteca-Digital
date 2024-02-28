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
@Controller
@RequestMapping("/loan")
public class LoanController {

    @Autowired
    private ILoanService loanService;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public String loans(ModelMap model, @RequestParam(required = false) String idUser) {

        List<User> listActive = userService.findAll();
        model.addAttribute("users", listActive);

        model.addAttribute("userSelected", null);

        if (idUser != null) {
            List<Loan> loansUser = loanService.findByUser(idUser);
            model.put("prestamos", loansUser);
            model.addAttribute("userSelected", userService.findById(idUser));
        } else {
            List<Loan> loans = loanService.findAll();
            model.put("loans", loans);
        }
        return "prestamos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/my-loans")
    public String myLoans(HttpSession session, ModelMap model) {

        User login = (User) session.getAttribute("usersession");

        if (login == null) {
            return "redirect:/";
        }

        List<Loan> myLoans = loanService.findByUser(login.getId());
        model.put("loans", myLoans);

        return "prestamos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/register")
    public String registerLoan(HttpSession session, ModelMap model) {

        User login = (User) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/home";
        }

        //Verificamos si es un administrador para pasarle la lista de users
        if (login.getRole().equals(Role.ADMIN)) {
            List<User> users = userService.findAll();
            model.put("users", users);
        }

        List<Book> books = bookService.findAll();
        model.put("books", books);

        model.addAttribute("loan", LoanDto.builder().build());

        return "registro-prestamo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/register")
    public String registerLoan(ModelMap model, HttpSession session, @ModelAttribute(name = "loan") @Valid LoanDto loanDto, BindingResult result) throws ErrorException {

        //Usamos el usuario logueado para obtener el id de la sesion del usuario y
        //no mandarlo a travez del input hidden del html
        User login = (User) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("loan", loanDto);
            return "redirect:/";
        }

        //verificamos si es usuario y sacamos el id de la sesion y si es admin mandamos lista de users
        if (login.getRole().equals(Role.USER)) {
            //Si buscamos el usuario asi tenemos q habilitar el input hidden del html y
            //colocar el atributo como requerido y comentar el login ya q no haria falta

            loanService.register(loanDto);

            return "redirect:/loan/my-loans"; //cambiar return
        } else {
            loanService.register(loanDto);
            return "redirect:/loan";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/update")
    public String updateLoan(ModelMap model, @RequestParam Long id) {

        List<Book> books = bookService.findAll();
        model.put("books", books);

        List<User> users = userService.findAll();
        model.put("users", users);

        Optional<Loan> loans = loanService.findById(id);
        model.addAttribute("loans", loans.get());

        return "modificar-prestamo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/update")
    public String updateLoan(ModelMap model, @RequestParam Long id, @ModelAttribute(name = "prestamo") @Valid LoanDto loanDto, BindingResult result) throws ErrorException {

        if (result.hasErrors()) {
            List<Book> books = bookService.findAll();
            model.put("books", books);

            List<User> users = userService.findAll();
            model.put("users", users);

            model.addAttribute("loan", loanDto);
            return "modificar-prestamo.html";
        }
        loanService.update(id, loanDto);
        return "redirect:/loan";

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/low")
    public String low(ModelMap model, @RequestParam Long id) {
        loanService.low(id);
        return "redirect:/loan";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/high")
    public String high(ModelMap model, @RequestParam Long id) throws ErrorException {
        loanService.high(id);
        return "redirect:/loan";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/delete")
    public String delete(ModelMap model, @RequestParam Long id) {
        loanService.delete(id);
        return "redirect:/loan";
    }
}

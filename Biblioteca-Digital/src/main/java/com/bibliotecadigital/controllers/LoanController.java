package com.bibliotecadigital.controllers;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.*;
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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Lucas Aramberry
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

        try {
            model.addAttribute("users", userService.findAll());

            User userSelected = null;

            if (idUser != null) {

                model.put("loans", (idUser.equalsIgnoreCase("todos")) ? loanService.findAll() : loanService.findByUser(idUser));

                userSelected = userService.findById(idUser);

                model.addAttribute("userSelected", userSelected != null ? userSelected : null);
            }

        } catch (ErrorException e) {
            e.getMessage();
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
            return "redirect:/";
        }

        //Verificamos si es un administrador para pasarle la lista de users
        if (login.getRole().equals(Role.ADMIN)) {
            model.put("users", userService.findAll());
        }

        List<Book> books = bookService.findAll();
        model.put("books", books);

        model.addAttribute("loan", LoanDto.builder().build());

        return "registro-prestamo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @PostMapping("/register")
    public String registerLoan(ModelMap model, HttpSession session, @ModelAttribute(name = "loan") @Valid LoanDto loanDto, BindingResult result) {

        //Usamos el usuario logueado para obtener el id de la sesion del usuario y
        //no mandarlo a travez del input hidden del html
        User login = (User) session.getAttribute("usersession");
        if (login == null) {
            return "redirect:/error";
        }

        if (result.hasErrors()) {
            model.addAttribute("loan", loanDto);
            return "redirect:/";
        }

        try {

            if (login.getRole().equals(Role.USER)) {
                loanDto.setIdUser(login.getId());
                loanService.register(loanDto);
                return "redirect:/loan/my-loans";
            } else {
                loanService.register(loanDto);
            }
        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/loan";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/update/{id}")
    public String updateLoan(Model model, @PathVariable Long id) {

        try {
            Loan loan = loanService.findById(id);

            model.addAttribute("user", loan.getUser().getEmail());
            model.addAttribute("books", bookService.findAll());
            model.addAttribute("loan", LoanDto
                    .builder()
                    .id(loan.getId())
                    .dateLoan(loan.getDateLoan())
                    .dateReturn(loan.getDateReturn())
                    .idBook(loan.getBook().getId())
                    .idUser(loan.getUser().getId())
                    .build()
            );

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "modificar-prestamo.html";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String updateLoan(Model model, @ModelAttribute(name = "loan") @Valid LoanDto loanDto, BindingResult result) {

        try {
            if (result.hasErrors()) {
                Loan loan = loanService.findById(Long.valueOf(loanDto.getIdUser()));
                model.addAttribute("user", loan.getUser().getEmail());
                model.addAttribute("books", bookService.findAll());
                model.addAttribute("loan", loanDto);

                return "modificar-prestamo.html";
            }

            loanService.update(loanDto);

        } catch (ErrorException e) {
            e.getMessage();
        }

        return "redirect:/loan";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/low/{id}")
    public String low(ModelMap model, @PathVariable Long id) {
        try {
            loanService.low(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/loan";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/high/{id}")
    public String high(ModelMap model, @PathVariable Long id) {
        try {
            loanService.high(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/loan";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(ModelMap model, @PathVariable Long id) {
        try {
            loanService.delete(id);
        } catch (ErrorException e) {
            e.getMessage();
        }
        return "redirect:/loan";
    }
}

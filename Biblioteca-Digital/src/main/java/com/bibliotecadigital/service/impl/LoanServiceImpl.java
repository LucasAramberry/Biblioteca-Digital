package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.ILoanDAO;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.ILoanService;
import com.bibliotecadigital.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Service
@Slf4j
public class LoanServiceImpl implements ILoanService {

    @Autowired
    private ILoanDAO loanDAO;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IUserService userService;

    /**
     * Metodo para registrar un loan
     *
     * @param loanDto
     */
    @Transactional
    @Override
    public void register(LoanDto loanDto) throws ErrorException {

        Book book = bookService.findById(loanDto.getIdBook());
        User user = userService.findById(loanDto.getIdUser());

        //realiza el seteo del ejemplar prestado en el libro
        bookService.lendBook(book);

        save(Loan
                .builder()
                .dateLoan(LocalDate.now())
                .dateReturn(loanDto.getDateReturn())
                .register(LocalDateTime.now())
                .unsubscribe(null)
                .book(book)
                .user(user)
                .build()
        );

        log.info("Create new Loan");
    }

    /**
     * Metodo para modificar un loan
     *
     * @param loanDto
     */
    @Transactional
    @Override
    public void update(LoanDto loanDto) throws ErrorException {

        Book book = bookService.findById(loanDto.getIdBook());
        User user = userService.findById(loanDto.getIdUser());

        Loan loan = findById(loanDto.getId());

        loan.setDateLoan(loanDto.getDateLoan());
        loan.setDateReturn(loanDto.getDateReturn());

        if (loan.getBook() != book) {
            //realizamos el seteo del ejemplar prestado en el libro nuevo
            bookService.lendBook(book);
            //devolvemos el ejemplar del libro que habiamos pedido
            bookService.devolutionBook(loan.getBook());
            loan.setBook(book);
        }

        loan.setUser(user);

        save(loan);

        log.info("Update Loan");
    }

    /**
     * metodo para eliminar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) throws ErrorException {

        Loan loan = findById(id);

        //verificamos si el loan esta dado de alta ya que debemos devolver el ejemplar
        // si esta de baja dando la baja automaticamente se devuelve el ejemplar del libro
        if (loan.getUnsubscribe() == null) {
            bookService.devolutionBook(loan.getBook());
        }

        loanDAO.delete(loan);

        log.info("Delete Loan with id " + id);
    }

    /**
     * Metodo para habilitar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(Long id) throws ErrorException {

        Loan loan = findById(id);

        //chequeamos que la fecha de devolucion sea posterior a la actual para poder volver habilitar el loan
        if (loan.getDateReturn().isAfter(LocalDate.now())) {

            //realizamos el seteo del ejemplar prestado en el libro nuevo
            bookService.lendBook(loan.getBook());

            loan.setUnsubscribe(null);

            save(loan);

            log.info("High Loan");
        } else {
            log.error("Error high loan with id " + id);
        }
    }

    /**
     * Metodo para deshabilitar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(Long id) throws ErrorException {

        Loan loan = findById(id);

        //devolvemos el libro por deshabilitar el loan
        bookService.devolutionBook(loan.getBook());

        loan.setUnsubscribe(LocalDateTime.now());

        save(loan);

        log.info("Low Loan with id " + id);
    }

    @Transactional
    @Override
    public void devolution(Long id) throws ErrorException {

        Loan loan = findById(id);

        loan.setDateReturn(LocalDate.now());

        //devolvemos el libro por deshabilitar el loan
        bookService.devolutionBook(loan.getBook());

        save(loan);

        log.info("Devolution Loan");
    }

    @Override
    public List<Loan> findByUser(String id) {
        return loanDAO.findByUser(id);
    }

    @Override
    public List<Loan> findByActive() {
        return loanDAO.findByActive();
    }

    @Override
    public List<Loan> findByInactive() {
        return loanDAO.findByInactive();
    }

    @Override
    public List<Loan> findAll() {
        return loanDAO.findAll();
    }

    @Override
    public Loan findById(Long id) throws ErrorException {
        return loanDAO.findById(id).orElseThrow(() -> new ErrorException("The loan with id " + id + " was not found."));
    }

    @Override
    public void save(Loan loan) {
        loanDAO.save(loan);
    }

    @Override
    public void deleteById(Long id) {
        loanDAO.deleteById(id);
    }
}

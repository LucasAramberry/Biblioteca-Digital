package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.Book;
import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.ILoanDAO;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.ILoanService;
import com.bibliotecadigital.services.IUserService;
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
     * Method for register loan
     *
     * @param loanDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void register(LoanDto loanDto) throws ErrorException {

        Book book = bookService.findById(loanDto.getIdBook());
        User user = userService.findById(loanDto.getIdUser());

        //performs the setting of the borrowed copy in the book
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
     * Method for update loan
     *
     * @param loanDto
     * @throws ErrorException
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
            //we carry out the setting of the borrowed copy in the new book
            bookService.lendBook(book);
            //We return the copy of the book that we had ordered
            bookService.devolutionBook(loan.getBook());
            loan.setBook(book);
        }

        loan.setUser(user);

        save(loan);

        log.info("Update Loan");
    }

    /**
     * Method for delete loan
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void delete(Long id) throws ErrorException {

        Loan loan = findById(id);

        //We verify if the loan is registered since we must return the copy
        //If you are unsubscribing, the copy of the book is automatically returned
        if (loan.getUnsubscribe() == null) {
            bookService.devolutionBook(loan.getBook());
        }

        loanDAO.delete(loan);

        log.info("Delete Loan with id " + id);
    }

    /**
     * Method for habilitate loan
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void high(Long id) throws ErrorException {

        Loan loan = findById(id);

        //We check that the return date is later than the current one to be able to re-enable the loan
        if (loan.getUnsubscribe() != null && loan.getDateReturn().isAfter(LocalDate.now())) {

            //we carry out the setting of the borrowed copy in the new book
            bookService.lendBook(loan.getBook());

            loan.setUnsubscribe(null);

            save(loan);

            log.info("High Loan");
        } else {
            log.error("Error high loan with id " + id);
        }
    }

    /**
     * Method to disable loan
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void low(Long id) throws ErrorException {

        Loan loan = findById(id);
        if (loan.getUnsubscribe() == null) {
            //We return the book for disabling the loan
            bookService.devolutionBook(loan.getBook());
            loan.setUnsubscribe(LocalDateTime.now());
            save(loan);
        }

        log.info("Low Loan with id " + id);
    }

    @Transactional
    @Override
    public void devolution(Long id) throws ErrorException {

        Loan loan = findById(id);

        loan.setDateReturn(LocalDate.now());

        //We return the book
        bookService.devolutionBook(loan.getBook());

        save(loan);

        log.info("Devolution Loan");
    }

    @Transactional(readOnly = true)
    @Override
    public List<Loan> findByUser(String id) {
        return loanDAO.findByUser(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Loan> findByActive() {
        return loanDAO.findByActive();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Loan> findByInactive() {
        return loanDAO.findByInactive();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Loan> findAll() {
        return loanDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Loan findById(Long id) throws ErrorException {
        return loanDAO.findById(id).orElseThrow(() -> new ErrorException("The loan with id " + id + " was not found."));
    }

    @Transactional
    @Override
    public void save(Loan loan) {
        loanDAO.save(loan);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        loanDAO.deleteById(id);
    }
}

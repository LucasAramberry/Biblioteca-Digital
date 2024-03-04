package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.LoanDto;
import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.ILoanDAO;
import com.bibliotecadigital.service.IBookService;
import com.bibliotecadigital.service.ILoanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private EmailServiceImpl emailServiceImpl;

//    private void validar(Date fechaLoan, Date fechaDevolucion, Libro libro, Usuario usuario) throws ErrorServicio {
//
//        if (fechaLoan == null || fechaLoan.after(fechaDevolucion)) {
//            throw new ErrorServicio("Fecha de loan invalida.");
//        }
//        if (fechaDevolucion == null || fechaDevolucion.before(fechaLoan) || fechaDevolucion.equals(fechaLoan)) {
//            throw new ErrorServicio("Fecha de loan invalida.");
//        }
//        if (usuario == null) {
//            throw new ErrorServicio("Usuario invalido.");
//        }
//        if (libro == null) {
//            throw new ErrorServicio("Libro invalido.");
//        }
//    }

    /**
     * Metodo para registrar un loan
     *
     * @param loanDto
     */
    @Transactional
    @Override
    public void register(LoanDto loanDto) throws ErrorException {

        //realizar el seteo del ejemplar prestado en el libro
        bookService.lendBook(loanDto.getBook());

        save(Loan
                .builder()
                .dateLoan(LocalDate.now())
                .dateReturn(loanDto.getDateReturn())
                .register(LocalDateTime.now())
                .unsubscribe(null)
                .book(loanDto.getBook())
                .user(loanDto.getUser())
                .build());

//        notificacionServicio.enviar("Realizaste el loan de un libro.", "Libreria web", usuario.getMail());
    }

    /**
     * Metodo para modificar un loan
     *
     * @param id
     * @param loanDto
     */
    @Transactional
    @Override
    public void update(Long id, LoanDto loanDto) throws ErrorException {

        Optional<Loan> response = findById(id);

        if (response.isPresent()) {

            Loan loan = response.get();

//            validar(fechaLoan, fechaDevolucion, libro, usuario);

            loan.setDateLoan(loanDto.getDateLoan());
            loan.setDateReturn(loanDto.getDateReturn());

            if (loan.getBook() != loanDto.getBook()) {
                //realizamos el seteo del ejemplar prestado en el libro nuevo
                bookService.lendBook(loanDto.getBook());
                //devolvemos el ejemplar del libro que habiamos pedido
                bookService.devolutionBook(loan.getBook());
            }

            loan.setBook(loanDto.getBook());
            loan.setUser(loanDto.getUser());

            save(loan);

            log.info("Se actualizo un Loan");

        } else {
            log.error("No se encontro el loan solicitado para modificar.");
        }
    }

    /**
     * metodo para eliminar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        Optional<Loan> response = findById(id);

        if (response.isPresent()) {
            Loan loan = response.get();

            //verificamos si el loan esta dado de alta ya que debemos devolver el ejemplar
            // si esta de baja dando la baja automaticamente se devuelve el ejemplar del libro
            if (loan.getUnsubscribe() == null) {
                bookService.devolutionBook(loan.getBook());
            }

            loanDAO.delete(loan);

            log.info("Se elimino un Loan");

        } else {
            log.error("No se encontro el loan solicitado para eliminar.");
        }
    }

    /**
     * Metodo para habilitar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(Long id) throws ErrorException {
        Optional<Loan> response = loanDAO.findById(id);

        if (response.isPresent()) {
            Loan loan = response.get();

            //chequeamos que la fecha de devolucion sea posterior a la actual para poder volver habilitar el loan
            if (loan.getDateReturn().isAfter(LocalDate.now())) {
                //realizamos el seteo del ejemplar prestado en el libro nuevo
                bookService.lendBook(loan.getBook());
            }

            loan.setUnsubscribe(null);

            save(loan);

            log.info("Se dio de alta un Prestamo");
        } else {
            log.error("No se encontro el prestamo solicitado para dar de alta.");
        }
    }

    /**
     * Metodo para deshabilitar un loan
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(Long id) {
        Optional<Loan> response = findById(id);

        if (response.isPresent()) {
            Loan loan = response.get();

            //devolvemos el libro por deshabilitar el loan
            bookService.devolutionBook(loan.getBook());

            loan.setUnsubscribe(LocalDateTime.now());

            save(loan);

            log.info("Se dio de baja un prestamo");
        } else {
            log.error("No se encontro el prestamo solicitado para dar de baja.");
        }
    }

    @Transactional
    @Override
    public void devolution(Long id) {

        Optional<Loan> response = findById(id);

        if (response.isPresent()) {

            Loan loan = response.get();
            loan.setDateReturn(LocalDate.now());

            //devolvemos el libro por deshabilitar el loan
            bookService.devolutionBook(loan.getBook());

            loanDAO.save(loan);

            log.info("Se devolvio un prestamo");

        } else {
            log.error("No se encontro el prestamo solicitado para dar de devolver.");
        }
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
    public Optional<Loan> findById(Long id) {
        return loanDAO.findById(id);
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

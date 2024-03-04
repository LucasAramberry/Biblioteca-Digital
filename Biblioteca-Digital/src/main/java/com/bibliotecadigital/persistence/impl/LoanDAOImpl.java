package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entities.Loan;
import com.bibliotecadigital.persistence.ILoanDAO;
import com.bibliotecadigital.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoanDAOImpl implements ILoanDAO {

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public List<Loan> findByUser(String id) {
        return loanRepository.findByUser(id);
    }

    @Override
    public List<Loan> findByActive() {
        return loanRepository.findByActive();
    }

    @Override
    public List<Loan> findByInactive() {
        return loanRepository.findByInactive();
    }

    @Override
    public List<Loan> findAll() {
        return (List<Loan>) loanRepository.findAll();
    }

    @Override
    public Optional<Loan> findById(Long id) {
        return loanRepository.findById(id);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public void delete(Loan loan) {
        loanRepository.delete(loan);
    }

    @Override
    public void deleteById(Long id) {
        loanRepository.findById(id);
    }
}

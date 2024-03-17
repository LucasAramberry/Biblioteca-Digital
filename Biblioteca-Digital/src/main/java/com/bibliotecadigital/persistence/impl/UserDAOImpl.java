package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.persistence.IUserDAO;
import com.bibliotecadigital.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements IUserDAO {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByActive() {
        return userRepository.findByActive();
    }

    @Override
    public List<User> findByUnsubscribeNotNull() {
        return userRepository.findByUnsubscribeNotNull();
    }

    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

}

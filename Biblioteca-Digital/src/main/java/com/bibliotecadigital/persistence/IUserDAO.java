package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    User findByEmail(String email);

    List<User> findByActive();

    List<User> findByInactive();

    List<User> findAll();

    Optional<User> findById(String id);

    void save(User user);

    void delete(User user);

    void deleteById(String id);
}

package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entity.Publisher;
import com.bibliotecadigital.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IUserDAO {

    public User findByEmail(String email);

    public List<User> findByActive();

    public List<User> findByInactive();

    List<User> findAll();

    Optional<User> findById(String id);

    void save(User user);

    void delete(User user);

    void deleteById(String id);
}

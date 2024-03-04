package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    void register(UserDto userDto);

    void update(String id, UserDto userDto);

    void delete(String id);

    void high(String id);

    void low(String id);

    void changeRol(String id);

    public User findByEmail(String email);

    public List<User> findByActive();

    public List<User> findByInactive();

    List<User> findAll();

    Optional<User> findById(String id);

    void save(User user);

    void delete(User user);

    void deleteById(String id);
}

package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    void register(UserDto userDto) throws ErrorException;

    void update(UserDto userDto) throws ErrorException;

    void delete(String id) throws ErrorException;

    void high(String id) throws ErrorException;

    void low(String id) throws ErrorException;

    void changeRol(String id) throws ErrorException;

    User findByEmail(String email);

    List<User> findByActive();

    List<User> findByInactive();

    List<User> findAll();

    User findById(String id) throws ErrorException;

    void save(User user);

    void delete(User user);

    void deleteById(String id);
}

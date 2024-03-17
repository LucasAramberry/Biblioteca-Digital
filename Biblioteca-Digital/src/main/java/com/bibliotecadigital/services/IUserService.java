package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;

public interface IUserService {

    void register(UserDto userDto) throws ErrorException;

    void update(UserDto userDto) throws ErrorException;

    void delete(String id) throws ErrorException;

    void high(String id) throws ErrorException;

    void low(String id) throws ErrorException;

    void changeRol(String id) throws ErrorException;

    User findByEmail(String email);

    List<User> findByActive();

    List<User> findByUnsubscribeNotNull();

    List<User> findAll();

    User findById(String id) throws ErrorException;

    void save(User user);

    void delete(User user);

}

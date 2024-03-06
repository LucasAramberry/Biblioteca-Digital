package com.bibliotecadigital.service;

import com.bibliotecadigital.entities.City;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;
import java.util.Optional;

public interface ICityService {

    List<City> findAll();

    City findById(Long id) throws ErrorException;

    void save(City city);

    void delete(City city);

    void deleteById(Long id);
}

package com.bibliotecadigital.service;

import com.bibliotecadigital.entity.City;

import java.util.List;
import java.util.Optional;

public interface ICityService {

    List<City> findAll();

    Optional<City> findById(Long id);

    void save(City city);

    void delete(City city);

    void deleteById(Long id);
}

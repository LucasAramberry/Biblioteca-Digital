package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entities.City;

import java.util.List;
import java.util.Optional;

public interface ICityDAO {

    List<City> findAll();

    Optional<City> findById(Long id);

    void save(City city);

    void delete(City city);

    void deleteById(Long id);
}

package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.entities.City;
import com.bibliotecadigital.persistence.ICityDAO;
import com.bibliotecadigital.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * @author Lucas Aramberry
 */
@Service
public class CityServiceImpl implements ICityService {

    @Autowired
    private ICityDAO cityDAO;

    @Transactional(readOnly = true)
    @Override
    public List<City> findAll() {
        return cityDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<City> findById(Long id) {
        return cityDAO.findById(id);
    }

    @Transactional
    @Override
    public void save(City city) {
        cityDAO.save(city);
    }

    @Transactional
    @Override
    public void delete(City city) {
        cityDAO.delete(city);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        cityDAO.deleteById(id);
    }
}

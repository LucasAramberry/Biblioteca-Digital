package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.entities.City;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.ICityDAO;
import com.bibliotecadigital.services.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public City findById(Long id) throws ErrorException {
        return cityDAO.findById(id).orElseThrow(() -> new ErrorException("The city with id " + id + " was not found."));
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

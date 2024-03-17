package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.persistence.IPublisherDAO;
import com.bibliotecadigital.repositories.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PublisherDAOImpl implements IPublisherDAO {

    @Autowired
    private PublisherRepository publisherRepository;

    @Override
    public List<Publisher> findByActive() {
        return publisherRepository.findByActive();
    }

    @Override
    public List<Publisher> findByInactive() {
        return publisherRepository.findByInactive();
    }

    @Override
    public Optional<Publisher> findByName(String name) {
        return publisherRepository.findByName(name);
    }

    @Override
    public List<Publisher> findAll() {
        return (List<Publisher>) publisherRepository.findAll();
    }

    @Override
    public Optional<Publisher> findById(String id) {
        return publisherRepository.findById(id);
    }

    @Override
    public void save(Publisher publisher) {
        publisherRepository.save(publisher);
    }

    @Override
    public void delete(Publisher publisher) {
        publisherRepository.delete(publisher);
    }

    @Override
    public void deleteById(String id) {
        publisherRepository.deleteById(id);
    }
}

package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;

public interface IPublisherService {

    void register(PublisherDto publisherDto);

    void update(String id, PublisherDto publisherDto) throws ErrorException;

    void high(String id) throws ErrorException;

    void low(String id) throws ErrorException;

    void delete(String id) throws ErrorException;

    List<Publisher> findByActive();

    List<Publisher> findByInactive();

    Publisher findByName(String name);

    List<Publisher> findAll();

    Publisher findById(String id) throws ErrorException;

    void save(Publisher publisher);

    void deleteById(String id);
}

package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entity.Publisher;

import java.util.List;
import java.util.Optional;

public interface IPublisherService {

    void register(PublisherDto publisherDto);

    void update(String id, PublisherDto publisherDto);

    void high(String id);

    void low(String id);

    void delete(String id);

    public List<Publisher> findByActive();

    public List<Publisher> findByInactive();

    public Publisher findByName(String name);

    List<Publisher> findAll();

    Optional<Publisher> findById(String id);

    void save(Publisher publisher);


    void deleteById(String id);
}

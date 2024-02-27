package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entity.Photo;
import com.bibliotecadigital.entity.Publisher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPublisherDAO {

    public List<Publisher> findByActive();

    public List<Publisher> findByInactive();

    public Publisher findByName(String name);

    List<Publisher> findAll();

    Optional<Publisher> findById(String id);

    void save(Publisher publisher);

    void delete(Publisher publisher);

    void deleteById(String id);
}

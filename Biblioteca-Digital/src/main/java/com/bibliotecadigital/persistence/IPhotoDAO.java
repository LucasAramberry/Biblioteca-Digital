package com.bibliotecadigital.persistence;

import com.bibliotecadigital.entity.Loan;
import com.bibliotecadigital.entity.Photo;

import java.util.List;
import java.util.Optional;

public interface IPhotoDAO {

    List<Photo> findAll();

    Optional<Photo> findById(Long id);

    void save(Photo photo);

    void delete(Photo photo);

    void deleteById(Long id);
}

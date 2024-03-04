package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Photo;

import java.util.List;
import java.util.Optional;

public interface IPhotoService {

    Photo register(PhotoDto photoDto);

    Photo update(Long idFoto, PhotoDto photoDto);

    List<Photo> findAll();

    Optional<Photo> findById(Long id);

    void save(Photo photo);

    void delete(Photo photo);

    void deleteById(Long id);
}

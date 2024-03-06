package com.bibliotecadigital.service;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;
import java.util.Optional;

public interface IPhotoService {

    Photo register(PhotoDto photoDto);

    Photo update(Long idFoto, PhotoDto photoDto) throws ErrorException;

    List<Photo> findAll();

    Photo findById(Long id) throws ErrorException;

    void save(Photo photo);

    void delete(Photo photo);

    void deleteById(Long id);
}

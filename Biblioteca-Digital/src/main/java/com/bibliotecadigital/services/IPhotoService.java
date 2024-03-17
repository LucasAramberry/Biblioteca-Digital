package com.bibliotecadigital.services;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.error.ErrorException;

import java.util.List;

public interface IPhotoService {

    Photo register(PhotoDto photoDto);

    Photo update(Long idPhoto, PhotoDto photoDto) throws ErrorException;

    List<Photo> findAll();

    Photo findById(Long id) throws ErrorException;

    void save(Photo photo);

    void delete(Photo photo);

    void deleteById(Long id);
}

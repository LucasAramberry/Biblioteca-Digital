package com.bibliotecadigital.persistence.impl;

import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.persistence.IPhotoDAO;
import com.bibliotecadigital.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PhotoDAOImpl implements IPhotoDAO {

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public List<Photo> findAll() {
        return (List<Photo>) photoRepository.findAll();
    }

    @Override
    public Optional<Photo> findById(Long id) {
        return photoRepository.findById(id);
    }

    @Override
    public void save(Photo photo) {
        photoRepository.save(photo);
    }

    @Override
    public void delete(Photo photo) {
        photoRepository.delete(photo);
    }

    @Override
    public void deleteById(Long id) {
        photoRepository.deleteById(id);
    }
}

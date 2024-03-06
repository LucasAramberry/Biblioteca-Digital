package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IPhotoDAO;
import com.bibliotecadigital.service.IPhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Service
@Slf4j
public class PhotoServiceImpl implements IPhotoService {

    @Autowired
    private IPhotoDAO iPhotoDAO;

    @Transactional
    @Override
    public Photo register(PhotoDto photoDto) {

        if (!photoDto.getFile().isEmpty() && photoDto.getFile() != null) {

            try {

                Photo photo = Photo
                        .builder()
                        .name(photoDto.getFile().getName())
                        .mime(photoDto.getFile().getContentType())
                        .content(photoDto.getFile().getBytes())
                        .build();

                save(photo);

                log.info("Create new Photo");

                return photo;
            } catch (IOException ex) {
                log.error("An error occurred while processing the photo: ", ex.getMessage());
            }

        }
        return null;
    }

    @Transactional
    @Override
    public Photo update(Long idFoto, PhotoDto photoDto) throws ErrorException {

        if (!photoDto.getFile().isEmpty() && photoDto.getFile() != null) {
            try {

                Photo photo = findById(idFoto);
                photo.setName(photoDto.getFile().getName());
                photo.setMime(photoDto.getFile().getContentType());
                photo.setContent(photoDto.getFile().getBytes());

                save(photo);

                log.info("Update Photo");

                return photo;
            } catch (IOException ex) {
                log.error("An error occurred while update the photo: ", ex.getMessage());
            }
        }
        return null;
    }


    @Transactional(readOnly = true)
    @Override
    public List<Photo> findAll() {
        return iPhotoDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Photo findById(Long id) throws ErrorException {
        return iPhotoDAO.findById(id).orElseThrow(() -> new ErrorException("The photo with id " + id + " was not found."));
    }

    @Transactional
    @Override
    public void save(Photo photo) {
        iPhotoDAO.save(photo);
    }

    @Transactional
    @Override
    public void delete(Photo photo) {
        iPhotoDAO.delete(photo);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        iPhotoDAO.deleteById(id);
    }
}

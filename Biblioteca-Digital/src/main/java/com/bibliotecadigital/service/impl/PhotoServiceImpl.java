package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.PhotoDto;
import com.bibliotecadigital.entity.Photo;
import com.bibliotecadigital.persistence.IPhotoDAO;
import com.bibliotecadigital.service.IPhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
        if (!photoDto.getFile().isEmpty() || photoDto.getFile() != null) {

            try {

                Photo photo = Photo
                        .builder()
                        .name(photoDto.getFile().getName())
                        .mime(photoDto.getFile().getContentType())
                        .content(photoDto.getFile().getBytes())
                        .build();

                save(photo);

                log.info("Se creo una foto");

                return photo;
            } catch (IOException ex) {
                log.error("Ocurrió un error al procesar la foto: ", ex.getMessage());
            }
        }
        return null;
    }

    @Transactional
    @Override
    public Photo update(Long idFoto, PhotoDto photoDto) {

        if (!photoDto.getFile().isEmpty() || photoDto.getFile() != null) {

            try {

                Photo photo = new Photo();

                if (idFoto != null) {

                    Optional<Photo> respuesta = findById(idFoto);
                    if (respuesta.isPresent()) {
                        photo = respuesta.get();
                    }

                }

                photo.setName(photoDto.getFile().getName());
                photo.setMime(photoDto.getFile().getContentType());
                photo.setContent(photoDto.getFile().getBytes());

                save(photo);

                log.info("Se actualizo una foto");

                return photo;

            } catch (IOException ex) {
                log.error("Ocurrió un error al actualizar la foto: ", ex.getMessage());
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
    public Optional<Photo> findById(Long id) {
        return iPhotoDAO.findById(id);
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

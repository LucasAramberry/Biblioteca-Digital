package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IPublisherDAO;
import com.bibliotecadigital.service.IPhotoService;
import com.bibliotecadigital.service.IPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Lucas Aramberry
 */
@Service
@Slf4j
public class PublisherServiceImpl implements IPublisherService {

    @Autowired
    private IPublisherDAO publisherDAO;
    @Autowired
    private IPhotoService photoService;

    /**
     * metodo para registrar editorial
     *
     * @param publisherDto
     */
    @Transactional
    @Override
    public void register(PublisherDto publisherDto) {

        save(Publisher
                .builder()
                .name(publisherDto.getName())
                .register(LocalDateTime.now())
                .photo(photoService.register(publisherDto.getPhotoDto()))
                .build());

        log.info("Create new Publisher");
    }

    /**
     * metodo para modificar editorial
     *
     * @param id
     * @param publisherDto
     */
    @Transactional
    @Override
    public void update(String id, PublisherDto publisherDto) throws ErrorException {

        Publisher publisher = findById(id);

        publisher.setName(publisherDto.getName());

        Photo photo = null;
        if (publisher.getPhoto() != null && !publisherDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.update(publisher.getPhoto().getId(), publisherDto.getPhotoDto());
        } else if (publisher.getPhoto() == null && !publisherDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.register(publisherDto.getPhotoDto());
        }
        publisher.setPhoto(photo);

        save(publisher);

        log.info("Update Publisher");
    }

    /**
     * Metodo para habilitar editorial
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {

        Publisher publisher = findById(id);
        publisher.setUnsubscribe(null);
        save(publisher);

        log.info("High publisher");
    }


    /**
     * Metodo para deshabilitar editorial
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {

        Publisher publisher = findById(id);
        publisher.setUnsubscribe(LocalDateTime.now());
        save(publisher);

        log.info("Low Publisher with id " + id);
    }

    /**
     * metodo para eliminar editorial
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {

        Publisher publisher = findById(id);
        publisherDAO.delete(publisher);

        log.info("Delete Publisher with id " + id);
    }

    /**
     * Metodo para devolver las editoriales activas
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Publisher> findByActive() {
        return publisherDAO.findByActive();
    }

    /**
     * Metodo para devolver las editoriales inactivas
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Publisher> findByInactive() {
        return publisherDAO.findByInactive();
    }

    @Transactional(readOnly = true)
    @Override
    public Publisher findByName(String name) {
        return publisherDAO.findByName(name);
    }

    /**
     * Metodo para devolver las editoriales
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Publisher> findAll() {
        return publisherDAO.findAll();
    }

    /**
     * metodo para buscar editorial por id
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Publisher findById(String id) throws ErrorException {
        return publisherDAO.findById(id).orElseThrow(() -> new ErrorException("The publisher with id " + id + " was not found."));
    }

    @Transactional
    @Override
    public void save(Publisher publisher) {
        publisherDAO.save(publisher);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        publisherDAO.deleteById(id);
    }
}

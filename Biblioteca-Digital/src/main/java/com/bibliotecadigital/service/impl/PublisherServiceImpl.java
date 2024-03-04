package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.entities.Publisher;
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

        Photo photo = photoService.register(publisherDto.getPhotoDto());

        save(Publisher
                .builder()
                .name(publisherDto.getName())
                .register(LocalDateTime.now())
                .photo(photo)
                .build());

        log.info("Se registro una nuevo Editorial");
    }

    /**
     * metodo para modificar editorial
     *
     * @param id
     * @param publisherDto
     */
    @Transactional
    @Override
    public void update(String id, PublisherDto publisherDto) {

        Publisher publisher = findById(id);

        if (publisher != null) {

            publisher.setName(publisherDto.getName());

            Long idFoto = null;

            if (publisher.getPhoto() != null) {
                idFoto = publisher.getPhoto().getId();
            }

            Photo photo = photoService.update(idFoto, publisherDto.getPhotoDto());

            publisher.setPhoto(photo);

            save(publisher);

            log.info("Se actualizo una Editorial");

        } else {
            log.error("No se encontro la editorial solicitado para modificar.");
        }
    }

    /**
     * Metodo para habilitar editorial
     *
     * @param id
     */
    @Transactional
    public void high(String id) {

        Publisher publisher = findById(id);

        if (publisher != null) {

            publisher.setUnsubscribe(null);

            save(publisher);

            log.info("Se dio de alta una Editorial");

        } else {
            log.error("No se encontro la editorial solicitado para dar de alta.");
        }
    }


    /**
     * Metodo para deshabilitar editorial
     *
     * @param id
     */
    @Transactional
    public void low(String id) {

        Publisher publisher = findById(id);

        if (publisher != null) {

            publisher.setUnsubscribe(LocalDateTime.now());

            save(publisher);

            log.info("Se dio de baja una Editorial");

        } else {
            log.error("No se encontro la editorial solicitado para dar de baja.");
        }
    }

    /**
     * metodo para eliminar editorial
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) {

        Publisher publisher = findById(id);

        if (publisher != null) {

            publisherDAO.delete(publisher);

            log.info("Se elimino una Editorial");

        } else {
            log.error("No se encontro la editorial solicitado para eliminar.");
        }
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
    public Publisher findById(String id) {
        return publisherDAO.findById(id).orElse(null);
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

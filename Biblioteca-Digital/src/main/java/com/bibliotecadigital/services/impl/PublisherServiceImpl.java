package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.dto.PublisherDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.entities.Publisher;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IPublisherDAO;
import com.bibliotecadigital.services.IBookService;
import com.bibliotecadigital.services.IPhotoService;
import com.bibliotecadigital.services.IPublisherService;
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
     * Method for register publisher
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
     * Method for update publisher
     *
     * @param id
     * @param publisherDto
     * @throws ErrorException
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
     * Method for habilitate publisher
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {

        Publisher publisher = findById(id);
        if (publisher.getUnsubscribe() != null) {
            publisher.setUnsubscribe(null);
            save(publisher);
        }

        log.info("High publisher");
    }


    /**
     * Method for disabled publisher
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {

        Publisher publisher = findById(id);
        if (publisher.getUnsubscribe() == null) {
            publisher.setUnsubscribe(LocalDateTime.now());
            save(publisher);
        }

        log.info("Low Publisher with id " + id);
    }

    /**
     * Method for delete publisher
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {

        Publisher publisher = findById(id);
        publisherDAO.delete(publisher);

        log.info("Delete Publisher with id " + id);
    }

    /**
     * Method return list for publishers active
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Publisher> findByActive() {
        return publisherDAO.findByActive();
    }

    /**
     * Method return list for publishers inactive
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
        return publisherDAO.findByName(name).orElseThrow();
    }

    /**
     * Method for return publishers
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Publisher> findAll() {
        return publisherDAO.findAll();
    }

    /**
     * Method for find publisher by id
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

package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IAuthorDAO;
import com.bibliotecadigital.services.IAuthorService;
import com.bibliotecadigital.services.IPhotoService;
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
public class AuthorServiceImpl implements IAuthorService {

    @Autowired
    private IAuthorDAO authorDAO;
    @Autowired
    private IPhotoService photoService;

    /**
     * Method for create author
     *
     * @param authorDto
     */
    @Transactional
    @Override
    public void register(AuthorDto authorDto) {

        save(Author
                .builder()
                .name(authorDto.getName())
                .register(LocalDateTime.now())
                .unsubscribe(null)
                .photo(photoService.register(authorDto.getPhotoDto()))
                .build());

        log.info("Create new Author");
    }

    /**
     * Method for update author
     *
     * @param authorDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void update(AuthorDto authorDto) throws ErrorException {

        Author author = findById(authorDto.getId());

        author.setName(authorDto.getName());

        Photo photo = null;
        if (author.getPhoto() != null && !authorDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.update(author.getPhoto().getId(), authorDto.getPhotoDto());
        } else if (author.getPhoto() == null && !authorDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.register(authorDto.getPhotoDto());
        }
        author.setPhoto(photo);

        save(author);

        log.info("Update Author");
    }

    /**
     * Method for delete author
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {

        Author author = findById(id);
        authorDAO.delete(author);

        log.info("Delete Author with id " + id);
    }

    /**
     * Method for unsubscribe author
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {

        Author author = findById(id);
        if (author.getUnsubscribe() == null) {
            author.setUnsubscribe(LocalDateTime.now());
            save(author);
        }

        log.info("Low Author with id " + id);
    }

    /**
     * Method for active author
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {

        Author author = findById(id);
        if (author.getUnsubscribe() != null) {
            author.setUnsubscribe(null);
            save(author);
        }

        log.info("High Author");
    }

    /**
     * Method find authors active
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findByActive() {
        return authorDAO.findByActive();
    }

    /**
     * Method find authors inactive
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findByInactive() {
        return authorDAO.findByInactive();
    }

    /**
     * Method find author by name
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Author findByName(String name) {
        return authorDAO.findByName(name).orElseThrow();
    }

    /**
     * Method find all authors
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorDAO.findAll();
    }

    /**
     * Method find author by id
     *
     * @param id
     * @return
     * @throws ErrorException
     */
    @Transactional(readOnly = true)
    @Override
    public Author findById(String id) throws ErrorException {
        return authorDAO.findById(id).orElseThrow(() -> new ErrorException("The author with id " + id + " was not found."));
    }

    /**
     * Method save author
     *
     * @param author
     */
    @Transactional
    @Override
    public void save(Author author) {
        authorDAO.save(author);
    }

    /**
     * Method delete Author by id
     *
     * @param id
     */
    @Transactional
    @Override
    public void deleteById(String id) {
        authorDAO.deleteById(id);
    }
}

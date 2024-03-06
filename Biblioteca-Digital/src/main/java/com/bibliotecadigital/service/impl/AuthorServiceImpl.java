package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.entities.Author;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IAuthorDAO;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IPhotoService;
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
     * Metodo para registrar autor
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
     * metodo para modificar autor
     *
     * @param authorDto
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
     * metodo para eliminar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {

        Author author = findById(id);
        authorDAO.delete(author);

        log.info("Delete Author with id " + id);
    }

    /**
     * metodo para deshabilitar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {

        Author author = findById(id);
        author.setUnsubscribe(LocalDateTime.now());

        save(author);

        log.info("Low Author with id " + id);
    }

    /**
     * Metodo para habilitar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {

        Author author = findById(id);

        author.setUnsubscribe(null);
        save(author);

        log.info("High Author");
    }

    /**
     * Metodo para listar autores activos
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findByActive() {
        return authorDAO.findByActive();
    }

    /**
     * Metodo para listar autores inactivos
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findByInactive() {
        return authorDAO.findByInactive();
    }

    /**
     * metodo para buscar autor por nombre
     *
     * @param name
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Author findByName(String name) {
        return authorDAO.findByName(name);
    }

    /**
     * Metodo para listar autores
     *
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorDAO.findAll();
    }

    /**
     * Metodo para buscar autor por id
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Author findById(String id) throws ErrorException {
        return authorDAO.findById(id).orElseThrow(() -> new ErrorException("The author with id " + id + " was not found."));
    }

    @Transactional
    @Override
    public void save(Author author) {
        authorDAO.save(author);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        authorDAO.deleteById(id);
    }
}

package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.AuthorDto;
import com.bibliotecadigital.entity.Author;
import com.bibliotecadigital.entity.Photo;
import com.bibliotecadigital.persistence.IAuthorDAO;
import com.bibliotecadigital.service.IAuthorService;
import com.bibliotecadigital.service.IPhotoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        log.info("Se registro un nuevo Autor");
    }

    /**
     * metodo para modificar autor
     *
     * @param id
     * @param authorDto
     */
    @Transactional
    @Override
    public void update(String id, AuthorDto authorDto) {

        Author author = findById(id);

        if (author != null) {

            author.setName(authorDto.getName());

            Long idFoto = null;

            if (author.getPhoto() != null) {
                idFoto = author.getPhoto().getId();
            }

            Photo photo = photoService.update(idFoto, authorDto.getPhotoDto());

            author.setPhoto(photo);

            save(author);

            log.info("Se actualizo un Autor");

        } else {
            log.error("No se encontro el autor solicitado para modificar.");
        }
    }

    /**
     * metodo para eliminar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) {
        Author author = findById(id);

        if (author != null) {

            authorDAO.delete(author);

            log.info("Se elimino un Autor");
        } else {
            log.error("No se encontro el autor solicitado para eliminar.");
        }
    }

    /**
     * metodo para deshabilitar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(String id) {

        Author author = findById(id);

        if (author != null) {

            author.setUnsubscribe(LocalDateTime.now());

            save(author);

            log.info("Se dio de baja un Autor");
        } else {
            log.error("No se encontro el autor solicitado para dar de baja.");
        }
    }

    /**
     * Metodo para habilitar autor
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(String id) {

        Author author = findById(id);

        if (author != null) {

            author.setUnsubscribe(null);

            save(author);

            log.info("Se dio de alta un Autor");

        } else {
            log.error("No se encontro el autor solicitado para dar de alta.");
        }
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
     * metodo para buscar autor por id
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Author findById(String id) {
        return authorDAO.findById(id).orElse(null);
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

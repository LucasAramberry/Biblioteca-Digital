package com.bibliotecadigital.service.impl;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entity.City;
import com.bibliotecadigital.entity.Photo;
import com.bibliotecadigital.entity.User;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.persistence.IUserDAO;
import com.bibliotecadigital.service.ICityService;
import com.bibliotecadigital.service.IPhotoService;
import com.bibliotecadigital.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private ICityService cityService;
    @Autowired
    private IPhotoService photoService;
    @Autowired
    private EmailServiceImpl emailService;

    /**
     * Metodo para registrar user
     *
     * @param userDto
     */
    @Transactional
    @Override
    public void register(UserDto userDto) {

        Optional<City> city = cityService.findById(userDto.getCityDto().getId());
        Photo photo = photoService.register(userDto.getPhotoDto());

        save(User
                .builder()
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .dni(userDto.getDni())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .role(Role.USER)
                .city(((city.get() != null) ? city.get() : null))
                .email(userDto.getEmail())
                .password(new BCryptPasswordEncoder().encode(userDto.getPassword()))
                .register(LocalDateTime.now())
                .photo(photo)
                .build());

//        notificacionServicio.enviar("Bienvenidos a la Libreria!", "Libreria web", user.getMail());
    }

    /**
     * Metodo para modificar user
     *
     * @param id
     * @param userDto
     */
    @Transactional
    @Override
    public void update(String id, UserDto userDto) {

        Optional<User> response = findById(id);

        if (response.isPresent()) {
            User user = response.get();

            user.setName(userDto.getName());
            user.setLastName(userDto.getLastName());
            user.setDni(userDto.getDni());
            user.setPhone(userDto.getPhone());
            user.setGender(userDto.getGender());

            Optional<City> city = cityService.findById(userDto.getCityDto().getId());
            user.setCity(((city.get() != null) ? city.get() : null));

            user.setEmail(userDto.getEmail());

            user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

            Long idFoto = null;

            if (user.getPhoto() != null) {
                idFoto = user.getPhoto().getId();
            }

            Photo photo = photoService.update(idFoto, userDto.getPhotoDto());
            user.setPhoto(photo);

            save(user);

        } else {
            log.error("No se encontro el usuario solicitado para modificar.");
        }
    }

    /**
     * Metodo para eliminar user
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(String id) {

        Optional<User> response = findById(id);

        if (response.isPresent()) {
            User user = response.get();
            userDAO.delete(user);
        } else {
            log.error("No se encontro el usuario solicitado para eliminar.");
        }
    }

    /**
     * Metodo para habilitar user
     *
     * @param id
     */
    @Transactional
    @Override
    public void high(String id) {
        Optional<User> response = findById(id);

        if (response.isPresent()) {
            User user = response.get();
            user.setUnsubscribe(null);

            save(user);
        } else {
            log.error("Error al habilitar el usuario solicitado");
        }
    }

    /**
     * Metodo para deshabilitar user
     *
     * @param id
     */
    @Transactional
    @Override
    public void low(String id) {
        Optional<User> response = findById(id);

        if (response.isPresent()) {
            User user = response.get();
            user.setUnsubscribe(LocalDateTime.now());

            save(user);
        } else {
            log.error("Error al deshabilitar el usuario solicitado");
        }
    }

    @Transactional
    @Override
    public void changeRol(String id) {
        Optional<User> response = findById(id);

        if (response.isPresent()) {
            User user = response.get();
            if (user.getRole().equals(Role.ADMIN)) {
                user.setRole(Role.USER);
            } else {
                user.setRole(Role.ADMIN);
            }

            save(user);
        } else {
            log.error("Error al cambiar el rol del user solicitado");
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    @Override
    public List<User> findByActive() {
        return userDAO.findByActive();
    }

    @Override
    public List<User> findByInactive() {
        return userDAO.findByInactive();
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return userDAO.findById(id);
    }

    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public void deleteById(String id) {
        userDAO.deleteById(id);
    }
}

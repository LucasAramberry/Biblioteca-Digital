package com.bibliotecadigital.services.impl;

import com.bibliotecadigital.dto.UserDto;
import com.bibliotecadigital.entities.Photo;
import com.bibliotecadigital.entities.User;
import com.bibliotecadigital.enums.Role;
import com.bibliotecadigital.error.ErrorException;
import com.bibliotecadigital.persistence.IUserDAO;
import com.bibliotecadigital.services.ICityService;
import com.bibliotecadigital.services.IPhotoService;
import com.bibliotecadigital.services.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * Method for register user
     *
     * @param userDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void register(UserDto userDto) throws ErrorException {

        save(User
                .builder()
                .name(userDto.getName())
                .lastName(userDto.getLastName())
                .dni(userDto.getDni())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .role(Role.USER)
                .city(cityService.findById(userDto.getIdCity()))
                .email(userDto.getEmail())
                .password(new BCryptPasswordEncoder().encode(userDto.getPassword()))
                .register(LocalDateTime.now())
                .photo(photoService.register(userDto.getPhotoDto()))
                .build()
        );

        log.info("Create new User");
    }

    /**
     * Method for update user
     *
     * @param userDto
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void update(UserDto userDto) throws ErrorException {

        User user = findById(userDto.getId());

        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setDni(userDto.getDni());
        user.setPhone(userDto.getPhone());
        user.setGender(userDto.getGender());
        user.setCity(((user.getCity().getId().equals(userDto.getIdCity())) ? user.getCity() : cityService.findById(userDto.getIdCity())));
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        Photo photo = null;
        if (user.getPhoto() != null && !userDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.update(user.getPhoto().getId(), userDto.getPhotoDto());
        } else if (user.getPhoto() == null && !userDto.getPhotoDto().getFile().isEmpty()) {
            photo = photoService.register(userDto.getPhotoDto());
        }

        user.setPhoto(photo);

        save(user);

        log.info("Update User");
    }

    /**
     * Method for delete user
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void delete(String id) throws ErrorException {
        User user = findById(id);
        delete(user);
        log.info("Delete User with id " + id);
    }

    /**
     * Method for habilitate user
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void high(String id) throws ErrorException {
        User user = findById(id);
        if (user.getUnsubscribe() != null) {
            user.setUnsubscribe(null);
            save(user);
        }
        log.info("High User");
    }

    /**
     * Method for disabled user
     *
     * @param id
     * @throws ErrorException
     */
    @Transactional
    @Override
    public void low(String id) throws ErrorException {
        User user = findById(id);
        if (user.getUnsubscribe() == null) {
            user.setUnsubscribe(LocalDateTime.now());
            save(user);
        }
        log.info("Low User with id " + id);
    }

    @Transactional
    @Override
    public void changeRol(String id) throws ErrorException {
        User user = findById(id);

        if (user.getRole().equals(Role.ADMIN)) {
            user.setRole(Role.USER);
        } else {
            user.setRole(Role.ADMIN);
        }
        save(user);

        log.error("Error change rol user with id " + id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User " + email + " not exist."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByActive() {
        return userDAO.findByActive();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByUnsubscribeNotNull() {
        return userDAO.findByUnsubscribeNotNull();
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userDAO.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(String id) throws ErrorException {
        return userDAO.findById(id).orElseThrow(() -> new ErrorException("The user with id " + id + " was not found."));
    }

    @Transactional
    @Override
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional
    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

}

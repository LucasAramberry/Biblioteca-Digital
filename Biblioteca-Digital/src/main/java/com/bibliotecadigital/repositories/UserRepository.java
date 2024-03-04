package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    // Buscar un usuario por mail y lo devuelve
    User findByEmail(String email);

    // Devuelve una Lista con Usuarios dados de alta.
    @Query("SELECT u FROM User u WHERE u.unsubscribe IS null")
    public List<User> findByActive();

    // Devuelve una Lista con Usuarios dados de baja.
    @Query("SELECT u FROM User u WHERE u.unsubscribe IS NOT null")
    public List<User> findByInactive();
}

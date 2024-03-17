package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.unsubscribe IS null")
    List<User> findByActive();

    List<User> findByUnsubscribeNotNull();
}

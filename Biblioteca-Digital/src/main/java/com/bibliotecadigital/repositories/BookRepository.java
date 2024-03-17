package com.bibliotecadigital.repositories;

import com.bibliotecadigital.entities.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    // Method return list books for Author
    @Query("SELECT b FROM Book b WHERE b.author.id =?1")
    List<Book> findByAuthor(String id);

    // Method return list books for Publisher
    @Query("SELECT b FROM Book b WHERE b.publisher.id = :id")
    List<Book> findByPublisher(@Param("id") String id);

    // Method return list books active
    @Query("SELECT b FROM Book b WHERE b.unsubscribe IS null")
    List<Book> listBookActive();

    // Method return books inactive
    @Query("SELECT b FROM Book b WHERE b.unsubscribe IS NOT null")
    List<Book> listBookInactive();
}

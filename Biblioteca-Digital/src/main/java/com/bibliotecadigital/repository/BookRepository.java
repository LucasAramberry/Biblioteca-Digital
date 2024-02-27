package com.bibliotecadigital.repository;

import com.bibliotecadigital.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, String> {

    // Método que devuelve el/los Libro/s vinculado a un Autor:
    @Query("SELECT a FROM Book a WHERE a.author.id = :id")
    public List<Book> findByAuthor(@Param("id") String id);

    // Método que devuelve el/los Libro/s vinculado a una Editorial:
    @Query("SELECT a FROM Book a WHERE a.publisher.id = :id")
    public List<Book> findByPublisher(@Param("id") String id);

    // Método que devuelve los libros activos
    @Query("SELECT a FROM Book a WHERE a.unsubscribe IS null")
    public List<Book> listBookActive();

    // Método que devuelve los libros inactivos
    @Query("SELECT a FROM Book a WHERE a.unsubscribe IS NOT null")
    public List<Book> listBookInactive();
}

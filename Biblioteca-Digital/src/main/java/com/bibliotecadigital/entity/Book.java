package com.bibliotecadigital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Lucas Aramberry
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "libros")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = true, unique = true)
    private String isbn;

    @Column(name = "titulo")
    private String title;

    @Column(name = "descripcion")
    private String description;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "fecha_publicacion", columnDefinition = "DATE")
    private LocalDate datePublisher;

    @Column(name = "cantidad_paginas")
    private Integer amountPages;

    @Column(name = "cantidad_copias")
    private Integer amountCopies;

    @Column(name = "cantidad_copias_prestadas")
    private Integer amountCopiesBorrowed;

    @Column(name = "cantidad_copias_restantes")
    private Integer amountCopiesRemaining;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "alta", columnDefinition = "DATETIME")
    private LocalDateTime register;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "baja", columnDefinition = "DATETIME")
    private LocalDateTime unsubscribe;

    @OneToOne(targetEntity = Photo.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_foto")
    private Photo photo;

    @OneToOne(targetEntity = Author.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_autor")
    private Author author;
//    @OneToMany(targetEntity = Author.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "book")
//    private List<Author> authors;

    @OneToOne(targetEntity = Publisher.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_editorial")
    private Publisher publisher;
//    @OneToMany(targetEntity = Publisher.class, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "book")
//    private List<Publisher> publishers;
}

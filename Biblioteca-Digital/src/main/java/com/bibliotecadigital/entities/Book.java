package com.bibliotecadigital.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
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
    @Min(0)
    private Integer amountCopies;

    @Column(name = "cantidad_copias_prestadas")
    @Min(0)
    private Integer amountCopiesBorrowed;

    @Column(name = "cantidad_copias_restantes")
    @Min(0)
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

    @ManyToOne(targetEntity = Author.class)
    @JoinColumn(name = "id_autor")
    private Author author;

    @ManyToOne(targetEntity = Publisher.class)
    @JoinColumn(name = "id_editorial")
    private Publisher publisher;
}

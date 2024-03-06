package com.bibliotecadigital.entities;

import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Lucas Aramberry
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "nombre")
    private String name;

    @Column(name = "apellido")
    private String lastName;

    @Column(name = "documento", unique = true)
    private String dni;

    @Column(name = "telefono")
    private String phone;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "rol")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(targetEntity = City.class)
    @JoinColumn(name = "id_ciudad")
    private City city;

    @OneToOne(targetEntity = Photo.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_foto")
    private Photo photo;

    @Column(unique = true)
    private String email;

    @Column(name = "contraseña")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "alta", columnDefinition = "DATETIME")
    private LocalDateTime register;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @Column(name = "baja", columnDefinition = "DATETIME")
    private LocalDateTime unsubscribe;
}

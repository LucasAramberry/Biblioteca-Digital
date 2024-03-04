package com.bibliotecadigital.dto;

import com.bibliotecadigital.entities.City;
import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;

    @NotBlank(message = "El nombre no puede ser nulo.")
    private String name;

    @NotBlank(message = "El apellido no puede ser nulo.")
    private String lastName;

    @NotBlank(message = "El documento no puede ser nulo.")
    @Size(min = 7, max = 10, message = "El documento no puede ser nulo.")
    private String dni;

    @NotBlank(message = "El teléfono no puede ser nulo.")
    @Size(min = 8, max = 20, message = "El teléfono no puede ser nulo.")
    private String phone;

    private Gender gender;

    private Role role;

//    private CityDto cityDto;
    private Long idCity;

    private PhotoDto photoDto;

    @Email(message = "El email no puede ser nulo.")
    private String email;

    @NotBlank(message = "Clave invalida. Debe contener al menos 6 digitos.")
    @Size(min = 6, max = 16, message = "Clave invalida. Debe contener al menos 6 digitos.")
    private String password;

    private String matchingPassword;

}

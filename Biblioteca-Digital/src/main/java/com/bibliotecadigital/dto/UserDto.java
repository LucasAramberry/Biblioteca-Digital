package com.bibliotecadigital.dto;

import com.bibliotecadigital.enums.Gender;
import com.bibliotecadigital.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Name cannot be null.")
    @Size(min = 3, max = 25, message = "Name min length is 3.")
    private String name;

    @NotBlank(message = "Lastname cannot be null.")
    @Size(min = 3, max = 25, message = "Lastname min length is 3.")
    private String lastName;

    @NotBlank(message = "Dni cannot be null.")
    @Size(min = 7, max = 10, message = "Dni min length is 7.")
    private String dni;

    @NotBlank(message = "Phone cannot be null.")
    @Size(min = 8, max = 20, message = "Phone min length is 8.")
    private String phone;

    private Gender gender;

    private Role role;

    @NotNull(message = "City cannot be null.")
    private Long idCity;

    private PhotoDto photoDto;

    @NotBlank(message = "Email cannot be null.")
    @Email(message = "Email incorrect format.")
    private String email;

    @NotBlank(message = "Invalid password, cannot be null.")
    @Size(min = 6, max = 16, message = "Invalid password. Must contain at least 6 digits.")
    private String password;

    private String matchingPassword;

}

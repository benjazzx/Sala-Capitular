package Biblioteca.example.User.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

package Biblioteca.example.Rol.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "CLIENTE|ADMIN|AUTOR", message = "El nombre debe ser CLIENTE, ADMIN o AUTOR")
    private String nombre;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
    private String descripcion;
}

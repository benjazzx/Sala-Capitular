package Biblioteca.example.Catalogo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogoRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
    private String descripcion;
}

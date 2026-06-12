package Biblioteca.example.Catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para crear o actualizar un catálogo")
public class CatalogoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    @Schema(
            description = "Nombre del catálogo",
            example = "Novela",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
    @Schema(
            description = "Descripción del catálogo",
            example = "Libros de narrativa, ficción y literatura general",
            maxLength = 300
    )
    private String descripcion;
}
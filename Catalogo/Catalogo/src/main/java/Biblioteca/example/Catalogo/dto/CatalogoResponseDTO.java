package Biblioteca.example.Catalogo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con la información de un catálogo")
public class CatalogoResponseDTO {

    @Schema(description = "Identificador único del catálogo", example = "1")
    private Long id;

    @Schema(description = "Nombre del catálogo", example = "Novela")
    private String nombre;

    @Schema(description = "Descripción del catálogo", example = "Libros de narrativa, ficción y literatura general")
    private String descripcion;
}
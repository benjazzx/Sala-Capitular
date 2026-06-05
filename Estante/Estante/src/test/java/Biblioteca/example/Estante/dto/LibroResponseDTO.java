package Biblioteca.example.Estante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta recibido desde el microservicio Libro")
public class LibroResponseDTO {

    @Schema(description = "Identificador único del libro", example = "1")
    private Long id;

    @Schema(description = "Título del libro", example = "El Principito")
    private String titulo;

    @Schema(description = "Código ISBN del libro", example = "9789560000000")
    private String isbn;

    @Schema(description = "Identificador del estado del libro", example = "1")
    private Long estadoId;
}
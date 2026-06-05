package Biblioteca.example.Multas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO recibido desde el microservicio Historial")
public class HistorialResponseDTO {

    @Schema(description = "Identificador del historial", example = "10")
    private Long id;

    @Schema(description = "Identificador del usuario asociado al historial", example = "5")
    private Long userId;

    @Schema(description = "Identificador del libro asociado al historial", example = "20")
    private Long libroId;
}


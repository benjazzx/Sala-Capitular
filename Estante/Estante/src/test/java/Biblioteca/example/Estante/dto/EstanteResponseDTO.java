package Biblioteca.example.Estante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con la información del estante")
public class EstanteResponseDTO {

    @Schema(description = "Identificador único del estante", example = "1")
    private Long id;

    @Schema(description = "Número identificador del estante", example = "12")
    private Integer numero;

    @Schema(description = "Nivel o altura del estante", example = "3")
    private Integer nivel;

    @Schema(description = "Pasillo donde se encuentra ubicado el estante", example = "Pasillo A")
    private String pasillo;

    @Schema(description = "Identificador del libro asociado al estante", example = "1")
    private Long libroId;
}
package Biblioteca.example.Estante.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para crear o actualizar un estante")
public class EstanteRequestDTO {

    @NotNull(message = "El número es obligatorio")
    @Schema(
            description = "Número identificador del estante",
            example = "12",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer numero;

    @NotNull(message = "El nivel es obligatorio")
    @Schema(
            description = "Nivel o altura del estante dentro del pasillo",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer nivel;

    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 50, message = "El pasillo no puede superar los 50 caracteres")
    @Schema(
            description = "Pasillo donde se encuentra ubicado el estante",
            example = "Pasillo A",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String pasillo;

    @NotNull(message = "El id del libro es obligatorio")
    @Schema(
            description = "Identificador del libro asociado al estante",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long libroId;
}
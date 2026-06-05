package Biblioteca.example.Multas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con la información de una multa")
public class MultaResponseDTO {

    @Schema(description = "Identificador único de la multa", example = "1")
    private Long id;

    @Schema(description = "Identificador del administrador que registró la multa", example = "1")
    private Long adminId;

    @Schema(description = "Identificador del usuario sancionado", example = "5")
    private Long userId;

    @Schema(description = "Identificador del historial asociado a la multa", example = "10")
    private Long historialId;

    @Schema(description = "Descripción o motivo de la multa", example = "Retraso en la devolución del libro")
    private String descripcion;

    @Schema(description = "Fecha de registro de la multa", example = "2026-06-05")
    private LocalDate fecha;

    @Schema(description = "Cantidad de puntos o unidades asociadas a la multa", example = "2")
    private Integer cantidad;

    @Schema(description = "Tipo de multa aplicada", example = "RETRASO", allowableValues = {"NO_ENTREGA", "DAÑO", "RETRASO"})
    private String tipo;
}
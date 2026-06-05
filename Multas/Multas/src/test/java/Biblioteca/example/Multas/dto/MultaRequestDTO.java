package Biblioteca.example.Multas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para crear o actualizar una multa")
public class MultaRequestDTO {

    @NotNull(message = "El id del administrador es obligatorio")
    @Schema(
            description = "Identificador del usuario administrador que registra la multa",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long adminId;

    @NotNull(message = "El id del usuario es obligatorio")
    @Schema(
            description = "Identificador del usuario sancionado",
            example = "5",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long userId;

    @NotNull(message = "El id del historial es obligatorio")
    @Schema(
            description = "Identificador del historial de préstamo asociado a la multa",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long historialId;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    @Schema(
            description = "Descripción o motivo detallado de la multa",
            example = "El usuario no realizó la devolución del libro en la fecha establecida.",
            maxLength = 500,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(
            description = "Fecha en que se registra la multa",
            example = "2026-06-05",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDate fecha;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Schema(
            description = "Cantidad de puntos o unidades asociadas a la multa",
            example = "2",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidad;

    @NotBlank(message = "El tipo de multa es obligatorio")
    @Pattern(
            regexp = "NO_ENTREGA|DAÑO|RETRASO",
            message = "El tipo debe ser NO_ENTREGA, DAÑO o RETRASO"
    )
    @Schema(
            description = "Tipo de multa aplicada al usuario",
            example = "RETRASO",
            allowableValues = {"NO_ENTREGA", "DAÑO", "RETRASO"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipo;
}
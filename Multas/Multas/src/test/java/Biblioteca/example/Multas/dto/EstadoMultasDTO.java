package Biblioteca.example.Multas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que resume el estado de multas de un usuario")
public class EstadoMultasDTO {

    @Schema(description = "Indica si el usuario puede reservar o pedir libros", example = "true")
    private boolean puedeReservar;

    @Schema(description = "Cantidad total de multas registradas para el usuario", example = "2")
    private int totalMultas;

    @Schema(description = "Cantidad total acumulada de puntos o unidades de multa", example = "3")
    private int totalCantidad;

    @Schema(
            description = "Mensaje informativo sobre el estado del usuario",
            example = "El usuario puede reservar libros."
    )
    private String aviso;
}
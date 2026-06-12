package Biblioteca.example.Multas.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class MultaRequestDTO {
    @NotNull(message = "El id del administrador es obligatorio")
    private Long adminId;
    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;
    @NotNull(message = "El id del historial es obligatorio")
    private Long historialId;
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima es 1")
    private Integer cantidad;
    @NotBlank(message = "El tipo de multa es obligatorio")
    @Pattern(regexp = "NO_ENTREGA|DAÑO|RETRASO", message = "El tipo debe ser NO_ENTREGA, DAÑO o RETRASO")
    private String tipo;
}

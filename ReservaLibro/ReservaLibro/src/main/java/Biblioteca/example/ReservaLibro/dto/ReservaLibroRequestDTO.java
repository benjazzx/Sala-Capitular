package Biblioteca.example.ReservaLibro.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class ReservaLibroRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;
    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDate fechaReserva;
    @NotBlank(message = "El estado de la reserva es obligatorio")
    @Pattern(regexp = "ACTIVA|CANCELADA|COMPLETADA", message = "El estado debe ser ACTIVA, CANCELADA o COMPLETADA")
    private String estadoReserva;
}

package Biblioteca.example.Historial.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class HistorialRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;
    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
    @NotNull(message = "La fecha de préstamo es obligatoria")
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    @NotNull(message = "El id del estado es obligatorio")
    private Long estadoId;
}

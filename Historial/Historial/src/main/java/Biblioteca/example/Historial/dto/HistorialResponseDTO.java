package Biblioteca.example.Historial.dto;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class HistorialResponseDTO {
    private Long id;
    private Long userId;
    private Long libroId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private Long estadoId;
}

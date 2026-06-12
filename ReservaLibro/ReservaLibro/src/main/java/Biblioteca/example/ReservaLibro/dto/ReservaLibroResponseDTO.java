package Biblioteca.example.ReservaLibro.dto;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class ReservaLibroResponseDTO {
    private Long id;
    private Long userId;
    private Long libroId;
    private LocalDate fechaReserva;
    private String estadoReserva;
    private String aviso;
}

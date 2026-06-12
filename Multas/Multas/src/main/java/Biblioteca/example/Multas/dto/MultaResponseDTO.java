package Biblioteca.example.Multas.dto;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class MultaResponseDTO {
    private Long id;
    private Long adminId;
    private Long userId;
    private Long historialId;
    private String descripcion;
    private LocalDate fecha;
    private Integer cantidad;
    private String tipo;
}

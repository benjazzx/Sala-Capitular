package Biblioteca.example.ResenaLibro.dto;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class ResenaLibroResponseDTO {
    private Long id;
    private Long userId;
    private Long libroId;
    private Integer calificacion;
    private String comentario;
    private LocalDate fecha;
}

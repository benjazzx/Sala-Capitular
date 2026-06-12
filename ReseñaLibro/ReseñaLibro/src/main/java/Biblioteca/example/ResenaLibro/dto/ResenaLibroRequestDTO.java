package Biblioteca.example.ResenaLibro.dto;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
public class ResenaLibroRequestDTO {
    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;
    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;
    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comentario;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}

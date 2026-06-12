package Biblioteca.example.Detalle.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class DetalleRequestDTO {
    @NotNull(message = "El id del historial es obligatorio")
    private Long historialId;
    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
    @Size(max = 500, message = "La observación no puede superar los 500 caracteres!")
    private String observacion;
}

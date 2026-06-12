package Biblioteca.example.Detalle.dto;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class DetalleResponseDTO {
    private Long id;
    private Long historialId;
    private Long libroId;
    private String observacion;
}

package Biblioteca.example.Estante.dto;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class LibroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Long estadoId;
}

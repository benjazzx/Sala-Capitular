package Biblioteca.example.Libro.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponseDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private Integer anio;
    private String descripcion;
    private Long autorId;
    private Long catalogoId;
    private Long estadoId;
}

package Biblioteca.example.Libro.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}

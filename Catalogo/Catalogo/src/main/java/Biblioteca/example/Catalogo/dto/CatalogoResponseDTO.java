package Biblioteca.example.Catalogo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}

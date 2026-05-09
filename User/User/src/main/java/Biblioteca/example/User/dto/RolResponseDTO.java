package Biblioteca.example.User.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}

package Biblioteca.example.User.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Long rolId;
    private String rolNombre;
}

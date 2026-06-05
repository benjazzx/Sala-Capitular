package Biblioteca.example.Multas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO recibido desde el microservicio User")
public class UserResponseDTO {

    @Schema(description = "Identificador del usuario", example = "5")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Felipe")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Lara")
    private String apellido;

    @Schema(description = "Correo electrónico del usuario", example = "felipe.lara@example.com")
    private String email;

    @Schema(description = "Identificador del rol del usuario", example = "1")
    private Long rolId;

    @Schema(description = "Nombre del rol del usuario", example = "ADMIN")
    private String rolNombre;
}
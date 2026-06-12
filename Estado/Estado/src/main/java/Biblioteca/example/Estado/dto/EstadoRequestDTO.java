package Biblioteca.example.Estado.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "DISPONIBLE|RESERVADO|PRESTADO|DAÑADO", message = "El nombre debe ser DISPONIBLE, RESERVADO, PRESTADO o DAÑADO")
    private String nombre;

    @Size(max = 300, message = "La descripción no puede superar los 300 caracteres")
    private String descripcion;
}

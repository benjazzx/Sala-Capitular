package Biblioteca.example.Libro.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroRequestDTO {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar los 200 caracteres")
    private String titulo;

    @Size(max = 20, message = "El ISBN no puede superar los 20 caracteres")
    private String isbn;

    private Integer anio;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El id del autor es obligatorio")
    private Long autorId;

    @NotNull(message = "El id del catálogo es obligatorio")
    private Long catalogoId;

    @NotNull(message = "El id del estado es obligatorio")
    private Long estadoId;
}

package Biblioteca.example.Estante.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class EstanteRequestDTO {
    @NotNull(message = "El número es obligatorio")
    private Integer numero;
    @NotNull(message = "El nivel es obligatorio")
    private Integer nivel;
    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 50, message = "El pasillo no puede superar los 50 caracteres")
    private String pasillo;
    @NotNull(message = "El id del libro es obligatorio")
    private Long libroId;
}

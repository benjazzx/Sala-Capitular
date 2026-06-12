package Biblioteca.example.Estante.dto;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
public class EstanteResponseDTO {
    private Long id;
    private Integer numero;
    private Integer nivel;
    private String pasillo;
    private Long libroId;
}

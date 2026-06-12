package Biblioteca.example.Estante.model;
import jakarta.persistence.*;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "estantes")
public class Estante {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer numero;
    @Column(nullable = false)
    private Integer nivel;
    @Column(nullable = false, length = 50)
    private String pasillo;
    @Column(name = "libro_id", nullable = false)
    private Long libroId;
}

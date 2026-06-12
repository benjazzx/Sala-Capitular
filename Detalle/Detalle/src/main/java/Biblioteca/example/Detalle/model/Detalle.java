package Biblioteca.example.Detalle.model;
import jakarta.persistence.*;
import lombok.*;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "detalles")
public class Detalle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "historial_id", nullable = false)
    private Long historialId;
    @Column(name = "libro_id", nullable = false)
    private Long libroId;
    @Column(length = 500)
    private String observacion;
}

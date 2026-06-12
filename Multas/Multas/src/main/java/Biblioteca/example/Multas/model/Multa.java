package Biblioteca.example.Multas.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "multas")
public class Multa {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "historial_id", nullable = false)
    private Long historialId;
    @Column(length = 500, nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = false)
    private Integer cantidad;
    @Column(name = "admin_id", nullable = false)
    private Long adminId;
    @Column(nullable = false, length = 20)
    private String tipo;
}

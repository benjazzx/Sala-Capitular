package Biblioteca.example.ResenaLibro.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "resenas_libro")
public class ResenaLibro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "libro_id", nullable = false)
    private Long libroId;
    @Column(nullable = false)
    private Integer calificacion;
    @Column(length = 1000)
    private String comentario;
    @Column(nullable = false)
    private LocalDate fecha;
}

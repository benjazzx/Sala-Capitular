package Biblioteca.example.Historial.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "historial")
public class Historial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "libro_id", nullable = false)
    private Long libroId;
    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;
    @Column(name = "estado_id", nullable = false)
    private Long estadoId;
}

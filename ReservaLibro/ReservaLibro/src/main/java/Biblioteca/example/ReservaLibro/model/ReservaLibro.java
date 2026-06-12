package Biblioteca.example.ReservaLibro.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
@Data @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "reservas_libro")
public class ReservaLibro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "libro_id", nullable = false)
    private Long libroId;
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;
    @Column(name = "estado_reserva", nullable = false, length = 20)
    private String estadoReserva;
}

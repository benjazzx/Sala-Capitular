package Biblioteca.example.ReservaLibro.repository;
import Biblioteca.example.ReservaLibro.model.ReservaLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface ReservaLibroRepository extends JpaRepository<ReservaLibro, Long> {
    Optional<ReservaLibro> findByLibroIdAndEstadoReserva(Long libroId, String estadoReserva);
}

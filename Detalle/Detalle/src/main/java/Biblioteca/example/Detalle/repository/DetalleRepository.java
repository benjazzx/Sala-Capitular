package Biblioteca.example.Detalle.repository;
import Biblioteca.example.Detalle.model.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DetalleRepository extends JpaRepository<Detalle, Long> {
}
